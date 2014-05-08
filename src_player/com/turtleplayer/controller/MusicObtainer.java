package com.turtleplayer.controller;

import com.turtleplayer.model.Track;

/**
 * 实现它以获取播放文件
 * @author matianyu
 */
public interface MusicObtainer {
	public Track getCurrentTrack();
	public Track getPriviousTrack();
	public Track getNextTrack();
}
