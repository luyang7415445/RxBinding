package com.jakewharton.rxbinding2.widget;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class RatingBarRatingChangeObservable extends InitialValueObservable<Float> {
  private final RatingBar view;

  RatingBarRatingChangeObservable(RatingBar view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super Float> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    view.setOnRatingBarChangeListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected Float getInitialValue() {
    return view.getRating();
  }

  static final class Listener extends MainThreadDisposable implements OnRatingBarChangeListener {
    private final RatingBar view;
    private final Observer<? super Float> observer;

    Listener(RatingBar view, Observer<? super Float> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
      if (!isDisposed()) {
        observer.onNext(rating);
      }
    }

    @Override protected void onDispose() {
      view.setOnRatingBarChangeListener(null);
    }
  }
}
