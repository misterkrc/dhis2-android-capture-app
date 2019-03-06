package org.dhis2.data.server;

import org.hisp.dhis.android.core.configuration.Configuration;
import org.hisp.dhis.android.core.configuration.ConfigurationManager;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import okhttp3.HttpUrl;

public class ConfigurationRepositoryImpl implements ConfigurationRepository {
    @NonNull
    private final ConfigurationManager configurationManager;

    public ConfigurationRepositoryImpl(@NonNull ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @NonNull
    @Override
    public Observable<Configuration> configure(@NonNull HttpUrl baseUrl) {
        return Observable.defer(() -> Observable.fromCallable(
                () -> configurationManager.configure(baseUrl)));
    }

    @NonNull
    @Override
    public Observable<Configuration> get() {
        return Observable.defer(() -> {
            Configuration configuration = configurationManager.get();
            if (configuration != null) {
                return Observable.just(configuration);
            }

            return Observable.empty();
        });
    }

    @NonNull
    @Override
    public Observable<Integer> remove() {
        return Observable.defer(() -> Observable.fromCallable(configurationManager::remove));
    }
}
