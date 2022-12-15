package com.arca.equipfix.gambachanneltv.data.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by gabri on 7/11/2018.
 */

public class PlayEpisode {

    @Expose
    private int id;

    @Expose
    private ItemUrls itemUrls;

    @Expose
    private  String title;

    @Expose
    private String seasonName;

    @Expose
    private int seasonId;

    @Expose
    private long lastPosition;

    public PlayEpisode()
    {

    }

    public ItemUrls getItemUrls() {
        return itemUrls;
    }

    public void setItemUrls(ItemUrls itemUrls) {
        this.itemUrls = itemUrls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public long getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(long lastPosition) {
        this.lastPosition = lastPosition;
    }
}
