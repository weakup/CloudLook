package com.example.xrecyclerview;

/**
 * Created by lisiyan on 2017/10/25.
 */

interface BaseRefreshHeader {

    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplate();

    int getVisiableHeight();


}
