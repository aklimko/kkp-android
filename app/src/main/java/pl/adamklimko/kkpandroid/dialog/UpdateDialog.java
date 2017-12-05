package pl.adamklimko.kkpandroid.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.model.types.ActionType;
import pl.adamklimko.kkpandroid.model.Products;
import pl.adamklimko.kkpandroid.model.Rooms;
import pl.adamklimko.kkpandroid.model.types.ThingType;
import pl.adamklimko.kkpandroid.task.ProductsTask;
import pl.adamklimko.kkpandroid.task.RoomsTask;

import java.util.ArrayList;
import java.util.List;

public class UpdateDialog {
    private final List<Integer> mSelectedItems = new ArrayList<>();
    private final AlertDialog.Builder builder;
    private final Context mContext;
    private final ThingType thingType;
    private final ActionType actionType;
    private final AsyncTask<Void, Void, Boolean> task;

    public UpdateDialog(ThingType thingType, ActionType actionType, Context context) {
        mContext = context;
        this.thingType = thingType;
        this.actionType = actionType;
        builder = new AlertDialog.Builder(mContext);
        final String[] names;
        switch (thingType) {
            case PRODUCTS:
                names = Products.getProductsNames();
                task = new ProductsTask(mSelectedItems, this.actionType, mContext);
                break;
            case ROOMS:
                names = Rooms.getRoomsNames();
                task = new RoomsTask(mSelectedItems, this.actionType, mContext);
                break;
            default:
                names = Products.getProductsNames();
                task = new ProductsTask(mSelectedItems, this.actionType, mContext);
        }

        builder.setMultiChoiceItems(names, null,
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
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mSelectedItems.isEmpty()) {
                            return;
                        }
                        task.execute();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        switch (actionType) {
            case DONE:
                setTitleDone();
                break;
            case TO_BE_DONE:
                setTitleToBeDone();
                break;
            default:
                break;
        }
    }

    private void setTitleDone() {
        builder.setTitle("Select bought products");
    }

    private void setTitleToBeDone() {
        builder.setTitle("Select missing products");

    }

    public void show() {
        builder.create().show();
    }
}
