package ru.princeparadoxes.smsdelivery.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.princeparadoxes.smsdelivery.R;
import ru.princeparadoxes.smsdelivery.SmsComponent;
import ru.princeparadoxes.smsdelivery.base.HasComponent;
import ru.princeparadoxes.smsdelivery.base.mvp.BaseActivity;
import ru.princeparadoxes.smsdelivery.base.mvp.BasePresenter;
import ru.princeparadoxes.smsdelivery.base.mvp.BaseView;
import ru.princeparadoxes.smsdelivery.base.navigation.activity.ActivityScreen;
import ru.princeparadoxes.smsdelivery.base.navigation.activity.ActivityScreenSwitcher;
import ru.princeparadoxes.smsdelivery.data.DataService;
import ru.princeparadoxes.smsdelivery.data.TokenStorage;
import ru.princeparadoxes.smsdelivery.ui.ApplicationSwitcher;
import ru.princeparadoxes.smsdelivery.ui.main.send.SendPresenter;
import ru.princeparadoxes.smsdelivery.ui.main.numbers.NumbersPresenter;
import ru.princeparadoxes.smsdelivery.ui.main.archive.ArchivePresenter;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity implements HasComponent<MainComponent> {

    @Inject
    Presenter presenter;

    @Inject
    ApplicationSwitcher applicationSwitcher;

    private MainComponent component;

    private boolean isActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_App);
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
    }

    @Override
    protected void onCreateComponent(SmsComponent ozomeComponent) {
        component = DaggerMainComponent.builder().
                mainModule(new MainModule(this)).
                smsComponent(ozomeComponent).build();
        component.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
        applicationSwitcher.attach(this);
    }

    @Override
    protected void onStop() {
        isActive = false;
        applicationSwitcher.detach();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        component = null;
        super.onDestroy();
    }

    @Override
    protected int layoutId() {
        return R.layout.main_layout;
    }

    @Override
    protected BasePresenter<? extends BaseView> presenter() {
        return presenter;
    }

    @Override
    protected int viewId() {
        return R.id.main_view;
    }

    @Override
    public MainComponent getComponent() {
        return component;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @MainScope
    public static final class Presenter extends BasePresenter<MainView> {

        private final DataService dataService;
        private final ActivityScreenSwitcher screenSwitcher;
        private final NumbersPresenter numbersPresenter;
        private final ArchivePresenter archivePresenter;
        private final SendPresenter sendPresenter;
        private final TokenStorage tokenStorage;

        @Nullable
        private CompositeSubscription subscriptions;

        private boolean isNeedSwitch;

        @Inject
        public Presenter(DataService dataService, ActivityScreenSwitcher screenSwitcher,
                         NumbersPresenter numbersPresenter,
                         ArchivePresenter archivePresenter, SendPresenter sendPresenter,
                         TokenStorage tokenStorage) {
            this.dataService = dataService;
            this.screenSwitcher = screenSwitcher;
            this.numbersPresenter = numbersPresenter;
            this.archivePresenter = archivePresenter;
            this.sendPresenter = sendPresenter;
            this.tokenStorage = tokenStorage;
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            subscriptions = new CompositeSubscription();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (subscriptions != null) {
                subscriptions.unsubscribe();
                subscriptions = null;
            }
        }

    }

    public static final class Screen extends ActivityScreen {
        @Override
        protected void configureIntent(@NonNull Intent intent) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        @Override
        protected Class<? extends Activity> activityClass() {
            return MainActivity.class;
        }
    }
}