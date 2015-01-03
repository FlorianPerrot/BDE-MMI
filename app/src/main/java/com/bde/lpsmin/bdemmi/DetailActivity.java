package com.bde.lpsmin.bdemmi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by lheido on 29/11/14.
 */
public class DetailActivity extends ActionBarActivity implements SwipeBackActivityBase {
    private static final String EXTRA_IMAGE = "DetailActivity:image";
    private static final String EXTRA_TITLE = "DetailActivity:title";

    private SwipeBackActivityHelper mHelper;
    private PhotoViewAttacher mAttacher;
    private ImageView image;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra(EXTRA_TITLE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_clear_mtrl_alpha);
        url = intent.getStringExtra(EXTRA_IMAGE);
        image = (ImageView) findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(image);
        ViewCompat.setTransitionName(image, EXTRA_IMAGE);
        load(url);
    }

    private void load(String url) {
        Picasso.with(this)
                .load(url)
                .into(image, new Callback() {
            @Override
            public void onSuccess() {
                mAttacher.update();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    public static void launch(BDEMain activity, View transitionView, Gallerie.Image photo) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_IMAGE, photo.getUrl());
        intent.putExtra(EXTRA_TITLE, photo.getTitle());
        ActivityCompat.startActivity(activity, intent, options.toBundle());
//        ActivityCompat.startActivity(activity, intent, Bundle.EMPTY);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
