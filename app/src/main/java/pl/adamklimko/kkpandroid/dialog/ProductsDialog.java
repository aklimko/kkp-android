package pl.adamklimko.kkpandroid.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.model.ActionType;
import pl.adamklimko.kkpandroid.model.Products;
import pl.adamklimko.kkpandroid.task.ProductsTask;

import java.util.ArrayList;
import java.util.List;

public class ProductsDialog {
    private final List<Integer> mSelectedItems = new ArrayList<>();
    private final AlertDialog.Builder builder;
    private final Context mContext;

    public ProductsDialog(Context context, ProductsType type) {
        mContext = context;
        builder = new AlertDialog.Builder(mContext);
        builder.setMultiChoiceItems(Products.getProductsNames(), null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        switch (type) {
            case BOUGHT:
                initBought();
                break;
            case MISSING:
                initMissing();
                break;
            default:
                break;
        }
    }

    private void initBought() {
        builder.setTitle("Select bought products");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mSelectedItems.isEmpty()) {
                    return;
                }
                new ProductsTask(mSelectedItems, ActionType.DONE, mContext).execute();
            }
        });
    }

    private void initMissing() {
        builder.setTitle("Select missing products");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mSelectedItems.isEmpty()) {
                    return;
                }
                new ProductsTask(mSelectedItems, ActionType.TO_BE_DONE, mContext).execute();
            }
        });
    }

    public void show() {
        builder.create().show();
    }
}
