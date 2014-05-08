package com.mty.test.animtext.core;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 
 * @author mty
 * @time 2011-9-27下午12:53:57
 */
public class AnimText {
	/**
	 * 颜料，可以设置图片、文字、几何图形的样式与颜色
	 */
	public Paint mPaint = new Paint();
	/**
	 * 文字的四个边界，以及高宽信息。注意，一定要先setText，否则此值为0.
	 */
	public Rect mBounds = new Rect();
	/**
	 * 文本
	 */
	public String mText;

	/**
	 * 初始位置的x坐标
	 */
	public float mFromX;
	/**
	 * 初始位置的y坐标
	 */
	public float mFromY;
	/**
	 * 最终位置的x坐标
	 */
	public float mToX;
	/**
	 * 最终位置的x坐标
	 */
	public float mToY;

	/**
	 * 每重绘一次X轴的增加量
	 */
	public float mXDistance;
	/**
	 * 每重绘一次Y轴的增加量
	 */
	public float mYDistance;
	/**
	 * 值越大，mXDistance与mYDistance越大，速度越慢。
	 */
	public static final float PER_DISTANCE = 30;

	/**
	 * 九宫格位置标记：起始格的行值
	 */
	public int mFromGridX = -1;
	/**
	 * 九宫格位置标记：起始格的列值
	 */
	public int mFromGridY = -1;
	/**
	 * 九宫格位置标记：最终格的行值
	 */
	public int mToGridX = -1;
	/**
	 * 九宫格位置标记：最终格的列值
	 */
	public int mToGridY = -1;
	/**
	 * 容器，实质是一个LinearLayout来放置控件。
	 */
	public AnimTextContainer container;

	/**
	 * 初始位置：四个角落，默认不设置。
	 */
	public int mStartOutType = AnimTextContainer.POS_OUT_NULL;
	/**
	 * 最终位置：四个角落，默认不设置。
	 */
	public int mEndOutType = AnimTextContainer.POS_OUT_NULL;

	public AnimText() {
		mPaint.setColor(Color.parseColor("#ff0000"));
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(30);
	}

	/**
	 * 获取颜料盘工具，可以来自由设置样式与颜色。
	 * 
	 * @return
	 */
	public Paint getPaint() {
		return mPaint;
	}

	/**
	 * 获取边界信息。
	 * 
	 * @return
	 */
	public Rect getBounds() {
		return mBounds;
	}

	/**
	 * 设置容器
	 * 
	 * @param container
	 */
	public void setContainer(AnimTextContainer container) {
		this.container = container;
	}

	/**
	 * 设置文本颜色，比如“#ff0000”为红色
	 * 
	 * @param text
	 */
	public void setTextColor(String text) {
		mPaint.setColor(Color.parseColor(text));
	}

	/**
	 * 设置文本
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.mText = text;
		mPaint.getTextBounds(mText, 0, mText.length(), mBounds);
	}

	/**
	 * 设置文本绝对位置，位置不会移动。
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		this.mFromX = this.mToX = x;
		this.mFromY = this.mToY = y + mBounds.height();
	}

	/**
	 * ，默认不设置，如果设置了该项。
	 * 
	 * @param pos
	 */
	public void setStartPoint(int type) {
		mStartOutType = type;
	}

	/**
	 * 
	 * @param pos
	 */
	public void setEndPoint(int type) {
		mEndOutType = type;
	}

	void measureStartOutPoint() {
		switch (mStartOutType) {
		/*
		 * 四个角落
		 */
		case AnimTextContainer.POS_OUT_TOP_LEFT:
			setStartPoint(-mBounds.width(), -mBounds.height());
			break;
		case AnimTextContainer.POS_OUT_TOP_RIGHT:
			setStartPoint(container.getMeasuredWidth(),
						-mBounds.height());
			break;
		case AnimTextContainer.POS_OUT_BOTTOM_LEFT:
			setStartPoint(-mBounds.width(),
						container.getMeasuredHeight());
			break;
		case AnimTextContainer.POS_OUT_BOTTOM_RIGHT:
			setStartPoint(container.getMeasuredWidth(),
						container.getMeasuredHeight());
			break;
		/*
		 * 上下左右
		 */
		case AnimTextContainer.POS_OUT_TOP:
			setStartPoint((mToGridX + 1) * container.getMeasuredWidth()
					/ (container.getColumns() + 1) - mBounds.width() / 2,
					-mBounds.height());
			break;
		case AnimTextContainer.POS_OUT_RIGHT:
			setStartPoint(
					container.getMeasuredWidth(),
					(mToGridY + 1) * container.getMeasuredHeight()
							/ (container.getRaws() + 1) - mBounds.height() / 2);
			break;
		case AnimTextContainer.POS_OUT_LEFT:
			setStartPoint(
					-mBounds.width(),
					(mToGridY + 1) * container.getMeasuredHeight()
							/ (container.getRaws() + 1) - mBounds.height() / 2);
			break;
		case AnimTextContainer.POS_OUT_BOTTOM:
			setStartPoint((mToGridX + 1) * container.getMeasuredWidth()
					/ (container.getColumns() + 1) - mBounds.width() / 2,
					container.getMeasuredHeight());
			break;
		default:
			setStartPoint(0f, 0f);
		}
	}

	void measureEndPointOutPoint() {
		switch (mEndOutType) {
		/*
		 * 四个角落
		 */
		case AnimTextContainer.POS_OUT_TOP_LEFT:
			setEndPoint(-mBounds.width(), -mBounds.height());
			break;
		case AnimTextContainer.POS_OUT_TOP_RIGHT:
			setEndPoint(container.getMeasuredWidth(),
						-mBounds.height());
			break;
		case AnimTextContainer.POS_OUT_BOTTOM_LEFT:
			setEndPoint(-mBounds.width(),
						container.getMeasuredHeight());
			break;
		case AnimTextContainer.POS_OUT_BOTTOM_RIGHT:
			setEndPoint(container.getMeasuredWidth(),
						container.getMeasuredHeight());
			break;
		/*
		 * 上下左右
		 */
		case AnimTextContainer.POS_OUT_TOP:
			setStartPoint((mFromGridX + 1) * container.getMeasuredWidth()
					/ (container.getColumns() + 1) - mBounds.width() / 2,
					-mBounds.height());
			break;
		case AnimTextContainer.POS_OUT_RIGHT:
			setStartPoint(
					container.getMeasuredWidth(),
					(mFromGridY + 1) * container.getMeasuredHeight()
							/ (container.getRaws() + 1) - mBounds.height() / 2);
			break;
		case AnimTextContainer.POS_OUT_LEFT:
			setStartPoint(
					-mBounds.width(),
					(mFromGridY + 1) * container.getMeasuredHeight()
							/ (container.getRaws() + 1) - mBounds.height() / 2);
			break;
		case AnimTextContainer.POS_OUT_BOTTOM:
			setStartPoint((mFromGridX + 1) * container.getMeasuredWidth()
					/ (container.getColumns() + 1) - mBounds.width() / 2,
					container.getMeasuredHeight());
			break;
		default:
			setEndPoint(-mBounds.width(), -mBounds.height());
		}
	}

	void measureStartGridPoint() {
		if (container.getRaws() <= 0 || container.getColumns() <= 0) {
			try {
				throw new AnimTextEception(
						" 你必须调用容器的setMatrix()方法设置行列值，call setMatrix() ");
			} catch (AnimTextEception e) {
				e.printStackTrace();
			}
		}
		// 处理界外四点（四个角）
		if (mFromGridX < 0 || mFromGridY < 0
				|| mFromGridX >= container.getRaws()
				|| mFromGridY >= container.getColumns()) {
			try {
				throw new AnimTextEception(" 九宫格数值设置越界 ");
			} catch (AnimTextEception e) {
				e.printStackTrace();
			}
		}
		float width = container.getMeasuredWidth();
		float height = container.getMeasuredHeight();
		int xLumps = container.getColumns() + 1;
		int yLumps = container.getRaws() + 1;
		setStartPoint((mFromGridX + 1) * width / xLumps - mBounds.width() / 2,
				(mFromGridY + 1)
						* height / yLumps - mBounds.height() / 2);
	}

	void measureEndGridPoint() {
		if (container.getRaws() <= 0 || container.getColumns() <= 0) {
			try {
				throw new AnimTextEception(
						" 你必须调用容器的setMatrix()方法设置行列值，call setMatrix() ");
			} catch (AnimTextEception e) {
				e.printStackTrace();
			}
		}
		if (mToGridX < 0 || mToGridY < 0 || mToGridX >= container.getRaws()
				|| mToGridY >= container.getColumns()) {
			try {
				throw new AnimTextEception(" 九宫格数值设置越界 ");
			} catch (AnimTextEception e) {
				e.printStackTrace();
			}
		}
		float width = container.getMeasuredWidth();
		float height = container.getMeasuredHeight();
		int xLumps = container.getColumns() + 1;// 4
		int yLumps = container.getRaws() + 1;// 8
		setEndPoint((mToGridX + 1) * width / xLumps - mBounds.width() / 2,
				(mToGridY + 1)
						* height / yLumps - mBounds.height() / 2);
	}

	/**
	 * in this matrix : <br/>
	 * (0,0) (0,1) (0,2) <br/>
	 * (1,0) (1,1) (1,2) <br/>
	 * (2,0) (2,1) (2,2) <br/>
	 * (3,0) (3,1) (3,2) <br/>
	 * the raws M is 4, and the columns N is 3.
	 * when you set setStartGridPoint(3,1),this view will translate from the
	 * (3,1)
	 * position.
	 * 
	 * @param m specific raw value
	 * @param n specific column value
	 */
	public void setStartGridPoint(int m, int n) {
		// 求X坐标需要列值,求y坐标需要行数值
		mFromGridX = n;
		mFromGridY = m;
	}

	/**
	 * in this matrix : <br/>
	 * (0,0) (0,1) (0,2) <br/>
	 * (1,0) (1,1) (1,2) <br/>
	 * (2,0) (2,1) (2,2) <br/>
	 * (3,0) (3,1) (3,2) <br/>
	 * the raws M is 4, and the columns N is 3.
	 * when you set setEndGridPoint(3,1),this view will translate to the (3,1)
	 * position.
	 * 
	 * @param m specific raw value
	 * @param n specific column value
	 */
	public void setEndGridPoint(int m, int n) {
		// 求X坐标需要列值,求y坐标需要行数值
		mToGridX = n;
		mToGridY = m;
	}

	private void setStartPoint(float x, float y) {
		this.mFromX = x;
		// 文字以底部为基轴，加高度，设置原点为左上角。
		this.mFromY = y + mBounds.height();
		// System.out.println("sx:" + mFromX + "sy:" + mFromY);
		measureText();
	}

	private void setEndPoint(float x, float y) {
		this.mToX = x;
		// 文字以底部为基轴，加高度，设置原点为左上角。
		this.mToY = y + mBounds.height();
		// System.out.println("ex:" + mToX + "ey:" + mToY);
		measureText();
	}

	private void measureText() {
		float offsetX = mToX - mFromX;
		float offsetY = mToY - mFromY;
		float offSet = Math.abs(offsetX) + Math.abs(offsetY);
		float distance = offSet / PER_DISTANCE;
		if (offSet == 0) {
			return;
		}
		mXDistance = (offsetX / offSet) * distance;
		mYDistance = (offsetY / offSet) * distance;
		// System.out.println(mXDistance + ":" + mYDistance);
	}
}