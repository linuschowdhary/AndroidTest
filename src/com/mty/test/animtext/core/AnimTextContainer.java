package com.mty.test.animtext.core;

import java.util.ArrayList;

import com.mty.test.animtext.AnimTextActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class AnimTextContainer extends LinearLayout {

	/**
	 * 定义矩阵为7行
	 */
	private int M = 0;
	/**
	 * c
	 * 定义矩阵为3列
	 */
	private int N = 0;
	public static final int POS_OUT_NULL = -1;

	public static final int POS_OUT_TOP = 1;
	public static final int POS_OUT_LEFT = 0;
	public static final int POS_OUT_RIGHT = 3;
	public static final int POS_OUT_BOTTOM = 2;
	public static final int POS_OUT_TOP_LEFT = 4;
	public static final int POS_OUT_TOP_RIGHT = 5;
	public static final int POS_OUT_BOTTOM_LEFT = 6;
	public static final int POS_OUT_BOTTOM_RIGHT = 7;

	ArrayList<AnimText> ats = new ArrayList<AnimText>();

	// int[] pos = new int[2];

	boolean isMeasureGrid = false;
	private Context context;

	public AnimTextContainer(Context context) {
		super(context);
		this.context = context;
	}

	public AnimTextContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setMatrix(int m, int n) {
		M = m;
		N = n;
	}

	public int getRaws() {
		return M;
	}

	public int getColumns() {
		return N;
	}

	public void addText(AnimText at) {
		ats.add(at);
	}

	public void clearText() {
		ats.clear();
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		ats.clear();
		ats = null;
		context = null;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 注意text从以左下角为原点，起点Y增加了height个高度，向下平移。
		for (int i = 0; i < ats.size(); i++) {
			AnimText at = ats.get(i);

			if (ev.getX() >= at.mFromX
					&& ev.getX() <= at.mFromX + at.mBounds.width()
					&& ev.getY() >= at.mFromY - at.mBounds.height()
					&& ev.getY() <= at.mFromY) {
				((AnimTextActivity) context).onTextTouchEvent(i);
			}

		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isMeasureGrid
				&& (getMeasuredHeight() != 0 || getMeasuredWidth() != 0)) {
			for (int i = 0; i < ats.size(); i++) {
				AnimText at = ats.get(i);
				at.setContainer(this);
				if (at.mFromGridX != -1) {
					at.measureStartGridPoint();
				}
				if (at.mToGridX != -1) {
					at.measureEndGridPoint();
				}
				if (at.mStartOutType != POS_OUT_NULL) {
					at.measureStartOutPoint();
				}
				if (at.mEndOutType != POS_OUT_NULL) {
					at.measureEndPointOutPoint();
				}
				// getLocationOnScreen(pos);
				isMeasureGrid = true;
			}
		}
	}

	public boolean isSameSymbol(float a, float b) {
		return (a > 0 && b > 0) || (a < 0 && b < 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// canvas.translate(pos[0], pos[1]);
		// System.out.println(pos[0]+":"+pos[1]);
		for (int i = 0; i < ats.size(); i++) {
			// System.out.println(i + "atv on Draw");
			AnimText at = ats.get(i);
			if (at.mText == null) {
				return;
			}
			// canvas.translate(at.mFromX, at.mFromY);
			canvas.drawText(at.mText, at.mFromX, at.mFromY, at.mPaint);
			if (at.mXDistance != 0 && at.mYDistance != 0
					&& isSameSymbol(at.mXDistance, at.mToX - at.mFromX)
					&& isSameSymbol(at.mYDistance, at.mToY - at.mFromY)) {
				at.mFromX += at.mXDistance;
				at.mFromY += at.mYDistance;
				invalidate();
			} else if (at.mXDistance == 0 && at.mToY - at.mFromY != 0
					&& isSameSymbol(at.mYDistance, at.mToY - at.mFromY)) {
				at.mFromY += at.mYDistance;
				invalidate();
			} else if (at.mYDistance == 0 && at.mToX - at.mFromX != 0
					&& isSameSymbol(at.mXDistance, at.mToX - at.mFromX)) {
				at.mFromX += at.mXDistance;
				invalidate();
			} else if (at.mFromX != at.mToX || at.mFromY != at.mToY) {
				at.mFromX = at.mToX;
				at.mFromY = at.mToY;
				// at.mFromY = at.mToY == 0 ? at.mToY + at.mBounds.height()
				// : at.mToY;
				invalidate();
			}

		}
	}
}