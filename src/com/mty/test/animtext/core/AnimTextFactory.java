package com.mty.test.animtext.core;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;

/**
 * 
 * @author mty
 * @time 2011-9-28上午11:07:11
 */
public class AnimTextFactory {
	public static final int ANIM_STYLE_MIN_NUM = 0;

	public static final int ANIM_STYLE_FROM_CONNER = 1;
	public static final int ANIM_STYLE_FROM_CENTER = 5;
	public static final int ANIM_STYLE_FROM_MY_0 = 2;
	public static final int ANIM_STYLE_FROM_MY_1 = 3;
	public static final int ANIM_STYLE_FROM_MY_2 = 4;
	public static final int ANIM_STYLE_FROM_MY_3 = 0;

	public static final int ANIM_STYLE_MAX_NUM = 5;

	public static void build(AnimTextContainer container,
			ArrayList<String> list, int style) {
		container.setMatrix(7, 7);
		container.setBackgroundColor(Color.WHITE);
		switch (style) {
		case ANIM_STYLE_FROM_CENTER:
			animStyleCenter(container, list);
			break;
		case ANIM_STYLE_FROM_CONNER:
			animStyleConner(container, list);
			break;
		case ANIM_STYLE_FROM_MY_0:
			animStyleMy0(container, list);
			break;
		case ANIM_STYLE_FROM_MY_1:
			animStyleMy1(container, list);
			break;
		case ANIM_STYLE_FROM_MY_2:
			animStyleMy2(container, list);
			break;
		case ANIM_STYLE_FROM_MY_3:
			animStyleMy3(container, list);
			break;
		}
	}

	private static void animStyleMy3(AnimTextContainer container,
			ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			AnimText at = new AnimText();
			container.addText(at);
			at.setText(list.get(i));
			at.setStartGridPoint(6, 3);
			switch (i) {
			case 0:
				at.setEndGridPoint(0, 3);
				break;
			case 1:
				at.setEndGridPoint(1, 1);
				break;
			case 2:
				at.setEndGridPoint(1, 5);
				break;
			case 3:
				at.setEndGridPoint(2, 2);
				break;
			case 4:
				at.setEndGridPoint(2, 4);
				break;
			case 5:
				at.setEndGridPoint(3, 0);
				break;
			case 6:
				at.setEndGridPoint(3, 3);
				break;
			case 7:
				at.setEndGridPoint(3, 6);
				break;
			case 8:
				at.setEndGridPoint(4, 2);
				break;
			case 9:
				at.setEndGridPoint(4, 4);
				break;
			case 10:
				at.setEndGridPoint(5, 1);
				break;
			case 11:
				at.setEndGridPoint(5, 5);
				break;
			case 12:
				at.setEndGridPoint(6, 3);
				break;
			default:
				at.setEndGridPoint(3, 3);
				break;
			}
		}
	}

	private static void animStyleMy2(AnimTextContainer container,
			ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			AnimText at = new AnimText();
			container.addText(at);
			at.setText(list.get(i));
			switch (i) {
			case 0:
			case 1:
			case 2:
				at.setStartPoint(AnimTextContainer.POS_OUT_TOP);
				break;
			case 5:
			case 7:
			case 10:
			case 11:
			case 12:
				at.setStartPoint(AnimTextContainer.POS_OUT_BOTTOM);
				break;
			case 3:
			case 6:
			case 4:
			case 8:
			case 9:
				at.setStartGridPoint(3, 3);
				break;
			}
			switch (i) {
			case 0:
				at.setEndGridPoint(0, 3);
				break;
			case 1:
				at.setEndGridPoint(1, 1);
				break;
			case 2:
				at.setEndGridPoint(1, 5);
				break;
			case 3:
				at.setEndGridPoint(2, 2);
				break;
			case 4:
				at.setEndGridPoint(2, 4);
				break;
			case 5:
				at.setEndGridPoint(3, 0);
				break;
			case 6:
				at.setEndGridPoint(3, 3);
				break;
			case 7:
				at.setEndGridPoint(3, 6);
				break;
			case 8:
				at.setEndGridPoint(4, 2);
				break;
			case 9:
				at.setEndGridPoint(4, 4);
				break;
			case 10:
				at.setEndGridPoint(5, 1);
				break;
			case 11:
				at.setEndGridPoint(5, 5);
				break;
			case 12:
				at.setEndGridPoint(6, 3);
				break;
			default:
				at.setEndGridPoint(3, 3);
				break;
			}
		}
	}

	private static void animStyleMy1(AnimTextContainer container,
			ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			AnimText at = new AnimText();
			container.addText(at);
			at.setText(list.get(i));
			switch (i) {
			case 1:
			case 3:
			case 5:
			case 6:
			case 8:
			case 10:
				at.setStartPoint(AnimTextContainer.POS_OUT_LEFT);
				break;
			case 0:
			case 2:
			case 4:
			case 7:
			case 9:
			case 11:
			case 12:
				at.setStartPoint(AnimTextContainer.POS_OUT_RIGHT);
				break;
			}
			switch (i) {
			case 0:
				at.setEndGridPoint(0, 3);
				break;
			case 1:
				at.setEndGridPoint(1, 1);
				break;
			case 2:
				at.setEndGridPoint(1, 5);
				break;
			case 3:
				at.setEndGridPoint(2, 2);
				break;
			case 4:
				at.setEndGridPoint(2, 4);
				break;
			case 5:
				at.setEndGridPoint(3, 0);
				break;
			case 6:
				at.setEndGridPoint(3, 3);
				break;
			case 7:
				at.setEndGridPoint(3, 6);
				break;
			case 8:
				at.setEndGridPoint(4, 2);
				break;
			case 9:
				at.setEndGridPoint(4, 4);
				break;
			case 10:
				at.setEndGridPoint(5, 1);
				break;
			case 11:
				at.setEndGridPoint(5, 5);
				break;
			case 12:
				at.setEndGridPoint(6, 3);
				break;
			default:
				at.setEndGridPoint(3, 3);
				break;
			}
		}
	}

	private static void animStyleMy0(AnimTextContainer container,
			ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			AnimText at = new AnimText();
			container.addText(at);
			at.setText(list.get(i));
			switch (i) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				at.setStartPoint(AnimTextContainer.POS_OUT_TOP);
				break;
			case 5:
				at.setStartGridPoint(3, 6);
				break;
			case 6:
				at.setStartGridPoint(3, 3);
				break;
			case 7:
				at.setStartGridPoint(3, 0);
				break;
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				at.setStartPoint(AnimTextContainer.POS_OUT_BOTTOM);
				break;
			}
			switch (i) {
			case 0:
				at.setEndGridPoint(0, 3);
				break;
			case 1:
				at.setEndGridPoint(1, 1);
				break;
			case 2:
				at.setEndGridPoint(1, 5);
				break;
			case 3:
				at.setEndGridPoint(2, 2);
				break;
			case 4:
				at.setEndGridPoint(2, 4);
				break;
			case 5:
				at.setEndGridPoint(3, 0);
				break;
			case 6:
				at.setEndGridPoint(3, 3);
				break;
			case 7:
				at.setEndGridPoint(3, 6);
				break;
			case 8:
				at.setEndGridPoint(4, 2);
				break;
			case 9:
				at.setEndGridPoint(4, 4);
				break;
			case 10:
				at.setEndGridPoint(5, 1);
				break;
			case 11:
				at.setEndGridPoint(5, 5);
				break;
			case 12:
				at.setEndGridPoint(6, 3);
				break;
			default:
				at.setEndGridPoint(3, 3);
				break;
			}
		}
	}

	private static void animStyleConner(AnimTextContainer container,
			ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			AnimText at = new AnimText();
			container.addText(at);
			at.setText(list.get(i));
			switch (i) {
			case 0:
			case 1:
			case 3:
				at.setStartPoint(AnimTextContainer.POS_OUT_TOP_LEFT);
				break;
			case 2:
			case 4:
			case 7:
				at.setStartPoint(AnimTextContainer.POS_OUT_TOP_RIGHT);
				break;
			case 5:
			case 8:
			case 10:
				at.setStartPoint(AnimTextContainer.POS_OUT_BOTTOM_LEFT);
				break;
			case 9:
			case 11:
			case 12:
				at.setStartPoint(AnimTextContainer.POS_OUT_BOTTOM_RIGHT);
				break;
			}
			switch (i) {
			case 0:
				at.setEndGridPoint(0, 3);
				break;
			case 1:
				at.setEndGridPoint(1, 1);
				break;
			case 2:
				at.setEndGridPoint(1, 5);
				break;
			case 3:
				at.setEndGridPoint(2, 2);
				break;
			case 4:
				at.setEndGridPoint(2, 4);
				break;
			case 5:
				at.setEndGridPoint(3, 0);
				break;
			case 6:
				at.setEndGridPoint(3, 3);
				break;
			case 7:
				at.setEndGridPoint(3, 6);
				break;
			case 8:
				at.setEndGridPoint(4, 2);
				break;
			case 9:
				at.setEndGridPoint(4, 4);
				break;
			case 10:
				at.setEndGridPoint(5, 1);
				break;
			case 11:
				at.setEndGridPoint(5, 5);
				break;
			case 12:
				at.setEndGridPoint(6, 3);
				break;
			default:
				at.setEndGridPoint(3, 3);
				break;
			}
		}
	}

	private static void animStyleCenter(AnimTextContainer container,
			ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			AnimText at = new AnimText();
			container.addText(at);
			at.setStartGridPoint(3, 3);
			at.setText(list.get(i));

			switch (i) {
			case 0:
				at.setEndGridPoint(0, 3);
				break;
			case 1:
				at.setEndGridPoint(1, 1);
				break;
			case 2:
				at.setEndGridPoint(1, 5);
				break;
			case 3:
				at.setEndGridPoint(2, 2);
				break;
			case 4:
				at.setEndGridPoint(2, 4);
				break;
			case 5:
				at.setEndGridPoint(3, 0);
				break;
			case 6:
				at.setEndGridPoint(3, 3);
				break;
			case 7:
				at.setEndGridPoint(3, 6);
				break;
			case 8:
				at.setEndGridPoint(4, 2);
				break;
			case 9:
				at.setEndGridPoint(4, 4);
				break;
			case 10:
				at.setEndGridPoint(5, 1);
				break;
			case 11:
				at.setEndGridPoint(5, 5);
				break;
			case 12:
				at.setEndGridPoint(6, 3);
				break;
			default:
				at.setEndGridPoint(3, 3);
				break;
			}
		}
	}

}
