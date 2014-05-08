package com.turtleplayer.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.turtleplayer.model.Track;

/**
 * TURTLE PLAYER
 * <p/>
 * Licensed under MIT & GPL
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * More Information @ www.turtle-player.co.uk
 * 
 * @author Simon Honegger (Hoene84)
 */

public class PlayerServiceConnector extends ObservableOutput {
	String TAG = "PlayerServiceConnector";
	final ContextWrapper contextWrapper;
	final Object waitOnConnectionLock = new Object();

	public PlayerServiceConnector(ContextWrapper contextWrapper) {
		this.contextWrapper = contextWrapper;
	}

	final Messenger mMessenger = new Messenger(new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PlayerService.Notify notification = PlayerService.Notify.values()[msg.what];
			switch (notification) {
			case CHANGED:
				int duration = msg.getData().getInt(
						PlayerService.PARAM_INTEGER_MILLIS);
				Track track = (Track) msg.getData().get(
						PlayerService.PARAM_OBJECT_TRACK);
				notifyTrackChanged(track, duration);
				break;
			case STARTED:
				notifyStarted();
				break;
			case STOPPED:
				notifyStopped();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	});

	PlayerService.PlayerServiceBinder playerServiceBinder = null;

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			synchronized (waitOnConnectionLock) {
				Log.i(TAG, "PlayerServiceConnector.onServiceConnected() called");
				playerServiceBinder = (PlayerService.PlayerServiceBinder) service;
				playerServiceBinder.register(mMessenger);
				waitOnConnectionLock.notifyAll();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			synchronized (waitOnConnectionLock) {
				Log.i(TAG,
						"PlayerServiceConnector.onServiceDisconnected() called");
				if (playerServiceBinder != null) {
					playerServiceBinder.unregister(mMessenger);
					playerServiceBinder = null;
				}
			}
		}
	};

	public void releasePlayer() {
		synchronized (waitOnConnectionLock) {
			if (playerServiceBinder != null) {
				Log.i(TAG, "PlayerServiceConnector.doUnbindService() called");
				playerServiceBinder.unregister(mMessenger);
				contextWrapper.unbindService(mConnection);
				playerServiceBinder = null;
			}
		}
	}

	/* 
	 * 将在初始化时被调用
	 */
	public void connectPlayer(final OutputCommand outputCommand) {
		new AsyncTask<Void, Void, Output>() {
			@Override
			protected Output doInBackground(Void... params) {
				Thread.currentThread().setName(
						Thread.currentThread().getName() + ":connectPlayer");
				synchronized (waitOnConnectionLock) {
					if (playerServiceBinder == null) {
						try {
							contextWrapper.bindService(new Intent(
									contextWrapper, PlayerService.class),
									mConnection, Context.BIND_AUTO_CREATE);
							waitOnConnectionLock.wait();
						} catch (InterruptedException e) {
							Log.e(TAG, "wait on output was interupted", e);
						}
					}
					return playerServiceBinder.getPlayerService();
				}
			}

			@Override
			protected void onPostExecute(Output output) {
				if (output != null) {
					outputCommand.connected(output);
				} else {
					Log.e(TAG, "omitting Player call: " + outputCommand);
				}
			}

		}.execute();
	}
}
