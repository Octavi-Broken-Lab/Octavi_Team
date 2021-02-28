package org.octavi.team;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import com.av.smoothviewpager.Smoolider.SmoothViewpager;
import com.zhpan.indicator.IndicatorView;
import com.zhpan.indicator.enums.IndicatorSlideMode;
import com.zhpan.indicator.enums.IndicatorStyle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<MaintainersList> maintainersList = new ArrayList<>();
    static List<CoreTeamList> coreTeamList = new ArrayList<>();

    // MAINTAINERS INFO
    static {
        // FIXME: 2/28/2021 ADD INFO
        //maintainersList.add(new MaintainersList("", 0, ""));
    }

    //CORE TEAM
    static {
        coreTeamList.add(new CoreTeamList("Sagar Rokade", "Core Developer", "Just a beginner in android journey.", R.drawable.sagar));
        coreTeamList.add(new CoreTeamList("Rahul Mishra", "Core Developer & UX Designer", "Another random guy trying to learn some stuff and gain some skills.", R.drawable.rahul));
        coreTeamList.add(new CoreTeamList("Satyam Sharma", "Core Developer & UX Designer", "\"The future belongs to those who believe in the beauty of their dreams.\"", R.drawable.satyam));
        coreTeamList.add(new CoreTeamList("Aurelein Dispenza", "Team Manager", "I love to help everyone & I like snakes!", R.drawable.billou));
        //coreTeamList.add(new CoreTeamList("Rodolphe", "Theme Advisor", "", R.drawable.spider));
    }

    Context context;
    private final ConstraintSet set = new ConstraintSet();
    private CardView maintainer;
    private SmoothViewpager sm;
    private View arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView im = findViewById(R.id.imageV);
        LinearLayout maintainerChild = findViewById(R.id.maintainer_child_con);
        maintainer = findViewById(R.id.maintainer_con);
        arrow = findViewById(R.id.arrow);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        sm = findViewById(R.id.smoolider);
        sm.setAdapter(adapter);
        IndicatorView iv = ((IndicatorView) findViewById(R.id.indicator_view));
        iv.setSliderHeight(15);
        iv.setSliderWidth(15);
        iv.setSliderColor(getAlpha(android.R.color.white, 100), getAlpha(R.color.white, 255));


        iv.setIndicatorStyle(IndicatorStyle.CIRCLE);
        iv.setSlideMode(IndicatorSlideMode.WORM);
        iv.setupWithViewPager(sm);

        im.setImageBitmap(blurImage(sm.getContext(), drawableToBitmap(getDrawable(coreTeamList.get(sm.getCurrentItem()).getPicResId()))));
        for (int i = 0; i < maintainersList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.maintain_det, null);
            ((TextView) view.findViewById(R.id.name)).setText(maintainersList.get(i).getName());
            ((TextView) view.findViewById(R.id.deviceName)).setText(maintainersList.get(i).getDeviceName());
            ((ImageView) view.findViewById(R.id.pic)).setImageResource(maintainersList.get(i).getPic());

            if (i == maintainersList.size() - 1)
                view.findViewById(R.id.div).setVisibility(View.GONE);
            maintainerChild.addView(view);
        }

        set.clone(((ConstraintLayout) findViewById(R.id.constt)));

        set.connect(maintainer.getId(), ConstraintSet.BOTTOM, R.id.smoolider, ConstraintSet.TOP);
        set.connect(maintainer.getId(), ConstraintSet.START, R.id.smoolider, ConstraintSet.START);
        set.connect(maintainer.getId(), ConstraintSet.END, R.id.smoolider, ConstraintSet.END);
        set.constrainHeight(maintainer.getId(), 0);
        set.constrainWidth(maintainer.getId(), ConstraintSet.MATCH_CONSTRAINT);
        set.applyTo(findViewById(R.id.constt));

        arrow.setOnClickListener(this::doMagicAgain);

        sm.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == sm.getCurrentItem()) {
                    setImageDrawableWithAnimation(sm.getContext(), im, blurImage(sm.getContext(), drawableToBitmap(getDrawable(coreTeamList.get(sm.getCurrentItem()).getPicResId()))));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private static Bitmap blurImage(Context context, Bitmap inputBitmap) {
        float BLUR_RADIUS = 7.5f;
        int intensity = 0;

        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        if (intensity > 0 && intensity <= 100) {
            BLUR_RADIUS = (float) intensity * 0.25f;
        }
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        tmpOut.destroy();
        tmpIn.destroy();
        theIntrinsic.destroy();
        rs.destroy();

        return outputBitmap;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, canvas.getWidth(), canvas.getHeight(), 0);
        drawable.draw(canvas);
        return bitmap;
    }

    private static void setImageDrawableWithAnimation(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);

        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ObjectAnimator.ofFloat(v, View.SCALE_Y, 1f, 1.1f, 1f).setDuration(1000).start();
                ObjectAnimator.ofFloat(v, View.SCALE_X, 1f, 1.1f, 1f).setDuration(1000).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageBitmap(new_image);
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }


    private void doMagicAgain(View view) {
        if (view.getRotation() == 0) {
            setImageDrawableWithAnimation(this, findViewById(R.id.imageV), blurImage(view.getContext(), drawableToBitmap(getDrawable(R.drawable.maint))));
            view.animate().rotation(180).setDuration(500).setStartDelay(100).start();

            set.connect(arrow.getId(), ConstraintSet.BOTTOM, maintainer.getId(), ConstraintSet.TOP);
            set.connect(arrow.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

            set.constrainHeight(maintainer.getId(), getResources().getDimensionPixelSize(R.dimen.maintainer_h));
            set.constrainWidth(maintainer.getId(), ConstraintSet.MATCH_CONSTRAINT);
            set.connect(maintainer.getId(), ConstraintSet.BOTTOM, R.id.smoolider, ConstraintSet.TOP);

            ((IndicatorView) findViewById(R.id.indicator_view)).animate().translationY(getResources().getDimensionPixelSize(R.dimen.fifty)).setDuration(500).start();
            sm.animate().translationY(sm.getHeight() + getResources().getDimensionPixelSize(R.dimen.fifty)).setDuration(500).start();
            arrow.animate().translationY(sm.getHeight() + getResources().getDimensionPixelSize(R.dimen.fifty)).setDuration(500).start();
            maintainer.animate().translationY(sm.getHeight() + getResources().getDimensionPixelSize(R.dimen.fifty)).setDuration(500).start();
        } else {
            setImageDrawableWithAnimation(this, findViewById(R.id.imageV), blurImage(view.getContext(), drawableToBitmap(getDrawable(coreTeamList.get(sm.getCurrentItem()).getPicResId()))));

            view.animate().rotation(0).setDuration(500).setStartDelay(100).start();

            set.connect(arrow.getId(), ConstraintSet.BOTTOM, R.id.smoolider, ConstraintSet.TOP);
            set.connect(arrow.getId(), ConstraintSet.END, R.id.smoolider, ConstraintSet.END);

            set.connect(maintainer.getId(), ConstraintSet.BOTTOM, R.id.smoolider, ConstraintSet.TOP);
            set.connect(maintainer.getId(), ConstraintSet.START, R.id.smoolider, ConstraintSet.START);
            set.connect(maintainer.getId(), ConstraintSet.END, R.id.smoolider, ConstraintSet.END);
            set.constrainHeight(maintainer.getId(), 0);
            set.constrainWidth(maintainer.getId(), ConstraintSet.MATCH_CONSTRAINT);


            sm.animate().translationY(0).setDuration(500).start();
            maintainer.animate().translationY(0).setDuration(500).start();
            ((IndicatorView) findViewById(R.id.indicator_view)).animate().translationY(0).setDuration(500).start();
        }
        TransitionManager.beginDelayedTransition(findViewById(R.id.constt));
        set.applyTo(findViewById(R.id.constt));
    }

    private int getAlpha(int color, int alpha) {
        return ColorUtils.setAlphaComponent(context.getColor(color), alpha);
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DetailsFragment();
            Bundle args = new Bundle();

            args.putString("TEAM_ROLE", coreTeamList.get(i).getRole());
            args.putString("TEAM_NAME", coreTeamList.get(i).getName());
            args.putString("TEAM_DESC", coreTeamList.get(i).getDesc());

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return coreTeamList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + (position + 1);
        }
    }
}