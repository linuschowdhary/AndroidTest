package com.mty.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mty.demo.R;

/**
 * 九宫格滑动解锁
 * 
 * @author matianyu 2013-10-23下午8:12:05
 */
public class TouchLockView extends View {

	/**
	 * 圈：九个圈的初始图片
	 */
	private Bitmap normalBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.touch_cicle_white);
	private int normalBitmapRadius = normalBitmap.getWidth() / 2;

	/**
	 * 圈：手划过点时的图片
	 */
	private Bitmap touchBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.touch_cicle_green);
	private int touchBitmapDiameter = touchBitmap.getWidth();
	private int touchBitmapRadius = touchBitmapDiameter / 2;
	/**
	 * 圈：当密码错误时的图片
	 */
	private Bitmap errorBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.touch_cicle_red);
	/**
	 * 点间连接线
	 */
	private Paint linePaint = new Paint();

	/**
	 * 触摸圈:3*3的九个点（圈）
	 */
	private TouchPoint[] points = new TouchPoint[9];

	/**
	 * 第一个被划过的点
	 */
	private TouchPoint startPoint = null;

	/**
	 * 控件宽高
	 */
	private int width, height;

	/**
	 * 触摸点位置
	 */
	private int moveX, moveY;

	/**
	 * 滑动结果是否正确：由OnCompleteListener来告知结果
	 */
	private boolean isPassed = false;

	/**
	 * 一次滑动产生的结果序列
	 */
	private StringBuilder touchString = new StringBuilder();

	/**
	 * 监听滑动事件完成
	 */
	private OnCompleteListener onCompleteListener;

	/**
	 * 状态：正常，触摸，通过，错误
	 */
	private enum State {
		Normal, Touched, Passed, Error
	}

	/**
	 * 四种状态
	 */
	private State state = State.Normal;

	private static final int SHOW_TIME = 600;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			forgetAll();
		};
	};

	public TouchLockView(Context context) {
		super(context);

		initLinePaint(linePaint);
	}

	public TouchLockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initLinePaint(linePaint);
	}

	/**
	 * 初始化连接线
	 * 
	 * @param paint
	 */
	private void initLinePaint(Paint paint) {
		paint.setColor(Color.parseColor("#ccffffff"));
		paint.setStrokeWidth(normalBitmap.getWidth() / 5);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Cap.ROUND);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 初始化屏幕大小
		width = getWidth();
		height = getHeight();
		if (width != 0 && height != 0) {
			initPoints(points);
		}
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	private int startX = 0, startY = 0;

	@Override
	protected void onDraw(Canvas canvas) {

		if (moveX != 0 && moveY != 0 && startX != 0 && startY != 0) {
			drawLine(canvas, startX, startY, moveX, moveY);
		}

		drawNinePoint(canvas);

		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			moveX = (int) event.getX();
			moveY = (int) event.getY();
			// MLog.i("onMove:" + moveX + "、" + moveY);

			for (TouchPoint temp : points) {
				if (temp.isNotSelected() && temp.isInMyPlace(moveX, moveY)) {
					temp.setSelected(true);
					// 因为用户ACTION_DOWN事件可能没有划中一个圈
					if (startPoint == null) {
						startPoint = temp;
					}
					startX = temp.getCenterX();
					startY = temp.getCenterY();
					int len = touchString.length();
					if (len != 0) {
						int preId = touchString.charAt(len - 1) - 48;
						points[preId].setNextId(temp.getId());
					}
					touchString.append(temp.getId());
					break;
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_DOWN:
			if (getState() != State.Normal) {
				forgetAll();
			}
			setState(State.Touched);
			int downX = (int) event.getX();
			int downY = (int) event.getY();
			for (TouchPoint temp : points) {
				if (temp.isInMyPlace(downX, downY)) {
					temp.setSelected(true);
					startPoint = temp;
					startX = temp.getCenterX();
					startY = temp.getCenterY();
					touchString.append(temp.getId());
					break;
				}
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			startX = startY = moveX = moveY = 0;
			finishHover();
			break;
		default:
			break;
		}
		return true;
	}

	private void finishHover() {
		if (onCompleteListener != null) {
			isPassed = onCompleteListener.onCompleted(touchString.toString());
		}
		if (isPassed) {
			setState(State.Passed);
		} else {
			setState(State.Error);
		}
		invalidate();
		System.out.println("########up ");
		handler.sendEmptyMessageDelayed(0, SHOW_TIME);
	}

	private void forgetAll() {
		handler.removeMessages(0);
		System.out.println("########forgetAll");
		for (TouchPoint temp : points) {
			temp.setSelected(false);
			temp.setNextId(temp.getId());
		}
		setState(State.Normal);
		startPoint = null;
		invalidate();
		touchString.delete(0, touchString.length());
	}

	private void initPoints(TouchPoint[] points) {
		if (points[0] != null) {
			return;
		}
		int len = points.length;

		int seletedSpacing = (width - touchBitmapDiameter * 3) / 4;

		// 被选择时显示图片的左上角坐标
		int seletedX = seletedSpacing;
		int seletedY = height - width + seletedSpacing;

		// 没被选时图片的左上角坐标
		int defaultX = seletedX + touchBitmapRadius - normalBitmapRadius;
		int defaultY = seletedY + touchBitmapRadius - normalBitmapRadius;

		// 绘制好每个点
		for (int i = 0; i < len; i++) {
			if (i == 3 || i == 6) {
				seletedX = seletedSpacing;
				seletedY += touchBitmapDiameter + seletedSpacing;

				defaultX = seletedX + touchBitmapRadius - normalBitmapRadius;
				// defaultY += touchBitmapDiameter + seletedSpacing;
				defaultY = seletedY + touchBitmapRadius - normalBitmapRadius;

			}
			points[i] = new TouchPoint(i, defaultX, defaultY, seletedX,
					seletedY, touchBitmapDiameter);

			seletedX += touchBitmapDiameter + seletedSpacing;
			defaultX += touchBitmapDiameter + seletedSpacing;

		}
	}

	/**
	 * 绘制已完成的部分
	 * 
	 * @param canvas
	 */
	private void drawNinePoint(Canvas canvas) {

		if (startPoint != null) {
			drawEachLine(canvas, startPoint);
		}

		// 绘制每个点的图片
		for (TouchPoint point : points) {
			if (point != null) {
				if(point.isSelected()){
					switch (getState()) {
					case Normal:
						canvas.drawBitmap(normalBitmap, point.getSeletedX(),
								point.getSeletedY(), null);
						break;
					case Touched:
						canvas.drawBitmap(touchBitmap, point.getSeletedX(),
								point.getSeletedY(), null);
						
						break;
					case Passed:
						canvas.drawBitmap(touchBitmap, point.getSeletedX(),
								point.getSeletedY(), null);
						
						break;
					case Error:
						canvas.drawBitmap(errorBitmap, point.getSeletedX(),
								point.getSeletedY(), null);
						break;
						
					default:
						canvas.drawBitmap(normalBitmap, point.getSeletedX(),
								point.getSeletedY(), null);
						break;
					}
				}else{
					canvas.drawBitmap(normalBitmap, point.getSeletedX(),
							point.getSeletedY(), null);
				}
			}
		}

	}

	/**
	 * 递归绘制每两个点之间的线段
	 * 
	 * @param canvas
	 * @param point
	 */
	private void drawEachLine(Canvas canvas, TouchPoint point) {
		if (point.hasNextId()) {
			int nextId = point.getNextId();
			drawLine(canvas, point.getCenterX(), point.getCenterY(),
					points[nextId].getCenterX(), points[nextId].getCenterY());
			// 递归
			drawEachLine(canvas, points[nextId]);
		}
	}

	/**
	 * 先绘制黑线，再在上面绘制白线，达到黑边白线的效果
	 * 
	 * @param canvas
	 * @param startX
	 * @param startY
	 * @param stopX
	 * @param stopY
	 */
	private void drawLine(Canvas canvas, float startX, float startY,
			float stopX, float stopY) {
		canvas.drawLine(startX, startY, stopX, stopY, linePaint);
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	private static class TouchPoint {

		// 一个点的ID
		private int id;

		// 当前点所指向的下一个点的ID，当没有时为自己ID
		private int nextId;

		// 是否被选中
		private boolean selected;

		// 默认时图片的左上角X坐标
		private int defaultX;

		// 默认时图片的左上角Y坐标
		private int defaultY;

		// 被选中时图片的左上角X坐标
		private int seletedX;

		// 被选中时图片的左上角Y坐标
		private int seletedY;
		private int bitmapDiameter;

		public TouchPoint(int id, int defaultX, int defaultY, int seletedX,
				int seletedY, int bitmapRadius) {
			this.id = id;
			this.nextId = id;
			this.defaultX = defaultX;
			this.defaultY = defaultY;
			this.seletedX = seletedX;
			this.seletedY = seletedY;
			this.bitmapDiameter = bitmapRadius;
		}

		public boolean isSelected() {
			return selected;
		}

		public boolean isNotSelected() {
			return !isSelected();
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public int getId() {
			return id;
		}

		public int getDefaultX() {
			return defaultX;
		}

		public int getDefaultY() {
			return defaultY;
		}

		public int getSeletedX() {
			return seletedX;
		}

		public int getSeletedY() {
			return seletedY;
		}

		public int getCenterX() {
			return seletedX + bitmapDiameter / 2;
		}

		public int getCenterY() {
			return seletedY + bitmapDiameter / 2;
		}

		public boolean hasNextId() {
			return nextId != id;
		}

		public int getNextId() {
			return nextId;
		}

		public void setNextId(int nextId) {
			this.nextId = nextId;
		}

		/**
		 * 坐标(x,y)是否在当前点的范围内
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		public boolean isInMyPlace(int x, int y) {
			boolean inX = x > seletedX && x < (seletedX + bitmapDiameter);
			boolean inY = y > seletedY && y < (seletedY + bitmapDiameter);

			return (inX && inY);
		}

	}

	public void setOnCompleteListener(OnCompleteListener onComplete) {
		this.onCompleteListener = onComplete;
	}

	public interface OnCompleteListener {
		public boolean onCompleted(String result);
	}
}
