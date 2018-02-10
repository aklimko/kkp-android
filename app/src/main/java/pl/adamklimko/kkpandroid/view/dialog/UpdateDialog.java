package pl.adamklimko.kkpandroid.view.dialog;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.model.types.ActionType;
import pl.adamklimko.kkpandroid.model.types.ThingType;
import pl.adamklimko.kkpandroid.task.ProductsTask;
import pl.adamklimko.kkpandroid.task.RoomsTask;

public class UpdateDialog {
    private final List<Integer> mSelectedItems = new ArrayList<>();
    private final AlertDialog.Builder builder;
    private final ThingType thingType;
    private final ActionType actionType;
    private final AsyncTask<Void, Void, Boolean> task;

    public UpdateDialog(ThingType thingType, ActionType actionType, Context context) {
        this.thingType = thingType;
        this.actionType = actionType;
        builder = new AlertDialog.Builder(context);
        int names;
        switch (thingType) {
            case PRODUCTS:
                names = R.array.products_names;
                task = new ProductsTask(mSelectedItems, this.actionType, context);
                break;
            case ROOMS:
                names = R.array.rooms_names;
                task = new RoomsTask(mSelectedItems, this.actionType, context);
                break;
            default:
                names = R.array.products_names;
                task = new ProductsTask(mSelectedItems, this.actionType, context);
        }

        builder.setMultiChoiceItems(names, null,
                (dialog, which, isChecked) -> {
                    if (isChecked) {
                        mSelectedItems.add(which);
                    } else if (mSelectedItems.contains(which)) {
                        mSelectedItems.remove(Integer.valueOf(which));
                    }
                })
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (mSelectedItems.isEmpty()) {
                        return;
                    }
                    task.execute();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                });
        setTitle();
    }

    private void setTitle() {
        switch (actionType) {
            case DONE:
                if (thingType == ThingType.PRODUCTS) {
                    setProductsTitleDone();
                } else {
                    setRoomsTitleDone();
                }
                break;
            case TO_BE_DONE:
                if (thingType == ThingType.PRODUCTS) {
                    setProductsTitleToBeDone();
                } else {
                    setRoomsTitleToBeDone();
                }
                break;
            default:
                break;
        }
    }

    private void setProductsTitleDone() {
        builder.setTitle(R.string.select_bought_products);
    }

    private void setProductsTitleToBeDone() {
        builder.setTitle(R.string.mark_missing_products);
    }

    private void setRoomsTitleDone() {
        builder.setTitle(R.string.select_cleaned_rooms);
    }

    private void setRoomsTitleToBeDone() {
        builder.setTitle(R.string.mark_dirty_rooms);
    }

    public void show() {
        builder.create().show();
    }
}
