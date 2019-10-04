package com.permissiondemo.abstractView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.permissiondemo.R;

class PermissionListDialog extends AlertDialog {

    private ImageButton done;
    private PermissionAdapter adapter;
    private boolean isMultiple = false;
    private PermissionCallBack callBack;
    private int color;
    private String[] permissions;
    private AlertDialog.Builder dialogBuilder;
    @SuppressLint("StaticFieldLeak")
    private static AlertDialog pList = null;


    static PermissionListDialog from(Activity context) {
        pList = new PermissionListDialog(context);
        return (PermissionListDialog) pList;
    }


    PermissionListDialog singlePermission(int color, PermissionCallBack callBack) {

        this.color = color;
        this.callBack = callBack;
        this.isMultiple = false;
        this.draw();
        return this;
    }

    PermissionListDialog loadPermission(String[] permissions) {
        this.permissions = permissions;
        return this;
    }


    void get() {
        pList.show();
    }

    PermissionListDialog multiplePermission(int color, PermissionCallBack callBack) {

        this.color = color;
        this.callBack = callBack;
        this.isMultiple = true;
        this.draw();
        return this;
    }


    private PermissionListDialog(Activity context) {
        super(context);

        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCancelable(true);
    }

    @SuppressLint("InflateParams")
    private void draw() {

        View view = getLayoutInflater().inflate(R.layout.permission_list, null);
        dialogBuilder.setView(view);


        ConstraintLayout header = view.findViewById(R.id.header);
        done = view.findViewById(R.id.done);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);


        pList = dialogBuilder.create();

        adapter = new PermissionAdapter();
        adapter.addPermissionData(permissions);
        if (!isMultiple) {
            buildSingleCallback((SingleCallBack) callBack);
        } else {
            buildMultipleCallback((MultipleCallBack) callBack);
        }
        header.setBackgroundColor(color);
        recyclerView.setAdapter(adapter);


    }


    private void buildSingleCallback(final SingleCallBack callBack) {
        done.setVisibility(View.GONE);
        adapter.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(SelectableItem item) {
                callBack.grantPermission(item.getName());
            }
        });
    }


    void viewDialog() {
        if (pList != null) {
            pList.dismiss();
            pList = null;
        }
    }

    private void buildMultipleCallback(final MultipleCallBack callBack) {
        adapter.setMultiSelectionEnabled();
        done.setVisibility(View.VISIBLE);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int len = adapter.getMultipleSelectedItems().size();

                if (len > 0) {
                    String[] permissions = new String[len];

                    int i = 0;
                    for (Item item : adapter.getMultipleSelectedItems()) {
                        permissions[i] = item.getName();
                        i++;
                    }
                    callBack.grantPermissions(permissions);
                }

            }
        });
    }


    interface PermissionCallBack {
    }

    interface SingleCallBack extends PermissionCallBack {
        void grantPermission(String permission);
    }

    interface MultipleCallBack extends PermissionCallBack {
        void grantPermissions(String[] permission);
    }

}
