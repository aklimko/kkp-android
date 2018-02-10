package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.os.AsyncTask;
import pl.adamklimko.kkpandroid.exception.NoNetworkConnectedException;
import pl.adamklimko.kkpandroid.model.types.ActionType;
import pl.adamklimko.kkpandroid.model.Products;
import pl.adamklimko.kkpandroid.network.ApiClient;
import pl.adamklimko.kkpandroid.network.KkpService;
import pl.adamklimko.kkpandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class ProductsTask extends AsyncTask<Void, Void, Boolean> {
    private final List<Integer> products;
    private final ActionType actionType;
    private final WeakReference<Context> mContext;

    private boolean noNetworkConnection = false;
    private boolean noInternetConnection = false;

    public ProductsTask(List<Integer> products, ActionType actionType, Context context) {
        this.products = products;
        this.actionType = actionType;
        this.mContext = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final Products products = getProductsObjectFromIntegers();
        final KkpService kkpService = ApiClient.createServiceWithAuth(KkpService.class, mContext.get());

        final Call<Products> productsCall;
        switch (actionType) {
            case DONE:
                productsCall = kkpService.addBoughtProducts(products);
                break;
            case TO_BE_DONE:
                productsCall = kkpService.selectProductsAsMissing(products);
                break;
            default:
                productsCall = kkpService.addBoughtProducts(products);
                break;
        }
        final Response<Products> response;
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

    private Products getProductsObjectFromIntegers() {
        final Products products = new Products();
        for (Integer productId : this.products) {
            products.setFieldValueToOne(productId);
        }
        return products;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (noNetworkConnection) {
            ToastUtil.showToastShort("No network connection", mContext.get());
            return;
        }
        if (noInternetConnection) {
            ToastUtil.showToastShort("Cannot connect to a server", mContext.get());
            return;
        }
        if (success) {
            ToastUtil.showToastShort("Successfully updated", mContext.get());
            updateDataInTable();
        } else {
            ToastUtil.showToastShort("Failed to update", mContext.get());
        }
    }

    private void updateDataInTable() {
        new DataTask(mContext.get()).execute();
    }
}
