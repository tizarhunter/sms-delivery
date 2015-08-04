package ru.princeparadoxes.smsdelivery.ui.create.database.second.step;

import javax.inject.Inject;

import ru.princeparadoxes.smsdelivery.base.mvp.BasePresenter;
import ru.princeparadoxes.smsdelivery.base.navigation.activity.ActivityScreenSwitcher;
import ru.princeparadoxes.smsdelivery.data.DataService;
import ru.princeparadoxes.smsdelivery.ui.create.database.CreateDatabaseScope;
import ru.princeparadoxes.smsdelivery.ui.main.MainScope;
import rx.subscriptions.CompositeSubscription;

@CreateDatabaseScope
public final class SecondStepPresenter extends BasePresenter<SecondStepView> {
    private final DataService dataService;
    private final ActivityScreenSwitcher screenSwitcher;

    private CompositeSubscription subscriptions;

    @Inject
    public SecondStepPresenter(DataService dataService, ActivityScreenSwitcher screenSwitcher) {
        this.dataService = dataService;
        this.screenSwitcher = screenSwitcher;
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
