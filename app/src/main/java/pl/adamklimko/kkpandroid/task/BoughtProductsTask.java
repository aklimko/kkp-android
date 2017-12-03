package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.os.AsyncTask;
import pl.adamklimko.kkpandroid.exception.NoNetworkConnectedException;
import pl.adamklimko.kkpandroid.model.BoughtProducts;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class BoughtProductsTask extends AsyncTask<Void, Void, Boolean> {
    private final List<Integer> products;
    private final Context mContext;

    private boolean noNetworkConnection = false;
    private boolean noInternetConnection = false;

    public BoughtProductsTask(List<Integer> products, Context context) {
        this.products = products;
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final BoughtProducts products = getBoughtProductsObjectFromIntegers();
        final KkpService kkpService = ApiClient.createServiceWithAuth(KkpService.class, mContext);
        final Call<BoughtProducts> productsCall = kkpService.addBoughtProducts(products);
        final Response<BoughtProducts> response;
        try {
            response = productsCall.execute();
        } catch (NoNetworkConnectedException e) {
            noNetworkConnection = true;
            return false;
        } catch (IOException e) {
            noInternetConnection = true;
            return false;
        }
        if (!response.isSuccessful()) {
            return false;
        }
        return response.code() == 200;
    }

    private BoughtProducts getBoughtProductsObjectFromIntegers() {
        final BoughtProducts boughtProducts = new BoughtProducts();
        for (Integer productId : products) {
            boughtProducts.setFieldValueToOne(productId);
        }
        return boughtProducts;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (noNetworkConnection) {
            ToastUtil.showToastShort("No network connection", mContext);
            return;
        }
        if (noInternetConnection) {
            ToastUtil.showToastShort("Cannot connect to a server", mContext);
            return;
        }
        if (success) {
            ToastUtil.showToastShort("Successfully updated", mContext);
            updateDataInTable();
        } else {
            ToastUtil.showToastShort("Failed to update", mContext);
        }
    }

    private void updateDataInTable() {
        new UsersDataTask(mContext).execute((Void) null);
    }
}

