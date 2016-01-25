package com.lenovo.main.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;

public class AnimationClass {
	private View view;
	private ScaleAnimation animation;
	private ScaleAnimation animation1;
	private ScaleAnimation animation2;
	private AnimationEndListenr animationEndListenr;
	private View view1;
	private View view2;

	public AnimationClass(View view, View view1, View view2) {

		this.view = view;
		this.view1 = view1;
		this.view2 = view2;
		initAnimation();
	}

	private void initAnimation() {
		animation = new ScaleAnimation(0.0f, 9.0f, 0.0f, 9.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(3000);
		animation.setFillAfter(false);

		animation1 = new ScaleAnimation(0.0f, 9.0f, 0.0f, 9.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation1.setDuration(3000);
		animation1.setFillAfter(false);
		animation1.setStartOffset(600);

		animation2 = new ScaleAnimation(0.0f, 9.0f, 0.0f, 9.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation2.setDuration(3000);
		animation2.setFillAfter(false);
		animation2.setStartOffset(1200);
	}

	public void cancelAnimation() {
		animation.cancel();
		animation1.cancel();
		animation2.cancel();
	}

	public void startAnimation_1() {
		view.startAnimation(animation);
		view1.startAnimation(animation1);
		view2.startAnimation(animation2);
		animation2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (animationEndListenr != null) {
					animationEndListenr.endAnimation();
				}
			}
		});
	}

	public interface AnimationEndListenr {
		void endAnimation();
	}

	public void setAnimationEndListenr(AnimationEndListenr animationEndListenr) {
		this.animationEndListenr = animationEndListenr;
	}
}
