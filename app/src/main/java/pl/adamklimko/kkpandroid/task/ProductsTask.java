package pl.adamklimko.kkpandroid.task;

import android.content.Context;
import android.os.AsyncTask;
import pl.adamklimko.kkpandroid.exception.NoNetworkConnectedException;
import pl.adamklimko.kkpandroid.model.ActionType;
import pl.adamklimko.kkpandroid.model.Products;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class ProductsTask extends AsyncTask<Void, Void, Boolean> {
    private final List<Integer> products;
    private final ActionType actionType;
    private final Context mContext;

    private boolean noNetworkConnection = false;
    private boolean noInternetConnection = false;

    public ProductsTask(List<Integer> products, ActionType actionType, Context context) {
        this.products = products;
        this.actionType = actionType;
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final Products products = getBoughtProductsObjectFromIntegers();
        final KkpService kkpService = ApiClient.createServiceWithAuth(KkpService.class, mContext);

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

    private Products getBoughtProductsObjectFromIntegers() {
        final Products products = new Products();
        for (Integer productId : this.products) {
            products.setFieldValueToOne(productId);
        }
        return products;
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
        new UsersDataTask(mContext).execute();
    }
}

