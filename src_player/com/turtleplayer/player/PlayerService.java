package com.turtleplayer.player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.mty.demo.R;
import com.mty.demo.act.MainActivity;
import com.turtleplayer.model.Track;

/**
 * 将播放和服务融合
 * 媒体播放参考http://developer.android.com/reference/android/media/MediaPlayer
 * @author Simon Honegger,matianyu
 * {@link #getMp()} 来获取播放器对象
 *  {@link #initialized} 保证播放器是就绪状态
 */
public class PlayerService extends Service implements Output {
	String TAG = "PlayerService";
	private final static int NOTIFICATION_ID = 22;

	public enum Notify {
		STARTED, STOPPED, CHANGED
	}

	public final static String PARAM_OBJECT_TRACK = "track";
	public final static String PARAM_INTEGER_MILLIS = "millis";

	public class PlayerServiceBinder extends Binder {
		public PlayerService getPlayerService() {
			return PlayerService.this;
		}

		public void register(Messenger messenger) {
			clients.add(messenger);
		}

		public void unregister(Messenger messenger) {
			clients.remove(messenger);
		}
	}

	Binder playerServiceBinder = new PlayerServiceBinder();

	private final Set<Messenger> clients = new HashSet<Messenger>();

	@Override
	public IBinder onBind(Intent intent) {
		return playerServiceBinder;
	}

	private MediaPlayer mp = null; // use getMp to access plz
	private boolean isPlaying = false;
	private Track currTrack = null;

	private boolean initialized = false; // indicates the player is at least in
											// Initialized mode

	@Override
	public void onCreate() {
		Log.i(TAG, "PlayerService.onCreate() called");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "PlayerService.onDestroy() called");
		super.onDestroy();
		release();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "PlayerService.onUnbind() called");
		return super.onUnbind(intent);
	}

	public void change(Track t) {
		if (t != null) {
			boolean wasPlaying = isPlaying;
			prepare(t);

			if (wasPlaying) {
				playInternal();
			}
			notifyTrackChanged(t, getMp().getDuration());
		}
	}

	public void play(Track t) {
		if (t != null) {
			boolean wasPlaying = isPlaying;
			prepare(t);
			playInternal();
			if (!wasPlaying) {
				notifyStarted();
			}
			notifyTrackChanged(t, getMp().getDuration());
		}
	}

	public void toggle() {
		if (initialized) {
			if (isPlaying) {
				pause();
			} else {
				play();
			}
		}
	}

	/**
	 * @return true if this call had an effect
	 */
	public boolean pause() {
		boolean stopped = pauseInternal();
		if (stopped) {
			notifyStopped();
		}
		return stopped;
	}

	public boolean pauseInternal() {
		if (initialized && isPlaying) {
			getMp().pause();
			isPlaying = false;
			return true;
		}
		return false;
	}

	/**
	 * @return true if this call had an effect
	 */
	public boolean play() {
		boolean started = playInternal();
		if (started) {
			notifyStarted();
		}
		return started;
	}

	/**
	 * @return true if this call had an effect
	 */
	public boolean playInternal() {
		if (!isPlaying && initialized) {
			getMp().start();
			isPlaying = true;
			return true;
		}
		return false;
	}

	public void goToMillis(int millis) {
		if (initialized) {
			getMp().seekTo(Math.max(Math.min(millis, getMp().getDuration()), 0));
		}
	}

	public int getCurrentMillis() {
		return initialized ? getMp().getCurrentPosition() : 0;
	}

	public Track getCurrTrack() {
		return currTrack;
	}

	public void setOnCompletionListener(
			MediaPlayer.OnCompletionListener listener) {
		getMp().setOnCompletionListener(listener);
	}

	/**
	 * @return MediaPlayer at least in idle state
	 */
	private MediaPlayer getMp() {
		if (mp == null) {
			mp = new MediaPlayer();
			mp.reset();
		}
		return mp;
	}

	/**
	 * releases the mp and kill the reference because the old instance is not
	 * usable anymore
	 */
	private void release() {
		initialized = false;
		pause();
		setOnCompletionListener(null);
		getMp().release();
		mp = null;
	}

	private void prepare(Track t) {
		if (t != null) {
			try {
				final MediaPlayer mediaPlayer = getMp();
				mp.reset();
				mediaPlayer.setDataSource(t.getFullPath());

				mediaPlayer.prepare();
				initialized = true;
				isPlaying = false;
				currTrack = t;
			} catch (IOException e) {
				Log.v(TAG, e.getMessage());
			}
		}
	}

	// ---------------------------------- Observable

	private void notifyTrackChanged(Track track, int lengthInMillis) {
		if (isPlaying) {
			startForeground(NOTIFICATION_ID, getNotification());
		}
		Bundle params = new Bundle();
		params.putSerializable(PARAM_OBJECT_TRACK, track);
		params.putInt(PARAM_INTEGER_MILLIS, lengthInMillis);
		notifyClients(Notify.CHANGED, params);
	}

	private void notifyStarted() {
		startForeground(NOTIFICATION_ID, getNotification());
		notifyClients(Notify.STARTED, null);
	}

	private void notifyStopped() {
		stopForeground(true);
		notifyClients(Notify.STOPPED, null);
	}

	private Notification getNotification() {
		Notification note = new Notification(R.drawable.ic_launcher,
				getText(R.string.app_name), System.currentTimeMillis());
		Intent i = new Intent(this,MainActivity.class);

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

		note.setLatestEventInfo(
				this,
				getText(R.string.app_name),
				getCurrTrack() == null ? "" : getCurrTrack().getName(), pi);
		note.flags |= Notification.FLAG_NO_CLEAR;
		return note;
	}

	private void notifyClients(Notify notification, Bundle params) {
		final Set<Messenger> clientsToRemove = new HashSet<Messenger>();
		for (Messenger client : clients) {
			try {
				Message msg = Message.obtain(null, notification.ordinal());
				msg.setData(params);
				client.send(msg);
			} catch (RemoteException e) {
				// If we get here, the client is dead, and we should remove it
				// from the list
				Log.d(TAG, "Client does not respond, remove it");
				clientsToRemove.add(client);
			}
		}
		for (Messenger client : clientsToRemove) {
			clients.remove(client);
		}
	}
}
