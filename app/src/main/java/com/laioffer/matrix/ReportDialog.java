package com.laioffer.matrix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReportDialog extends Dialog {
    private int cx;
    private int cy;
    private RecyclerView recyclerView;
    private ReportRecyclerViewAdapter recyclerViewAdapter;
    private ViewSwitcher viewSwitcher;
    private String mEventype;
    private String mPrefillText;

    private ImageView mImageCamera;
    private Button mBackButton;
    private Button mSendButton;
    private EditText mCommentEditText;
    private ImageView mEventTypeImg;
    private TextView mTypeTextView;
    private DialogCallBack mDialogCallBack;

    interface DialogCallBack {
        void onSubmit(String editString, String event_type);
        void StartCamera();
    }

    public void setDialogCallBack(DialogCallBack dialogCallBack) {
        mDialogCallBack = dialogCallBack;
    }


    public ReportDialog(@NonNull Context context) {
        this(context, R.style.MyAlertDialogStyle);
    }

    public ReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View dialogView = View.inflate(getContext(), R.layout.dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                animateDialog(dialogView, true);
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    animateDialog(dialogView, false);
                    return true;
                }
                return false;
            }
        });
        setupRecyclerView(dialogView);
        viewSwitcher = (ViewSwitcher) dialogView.findViewById(R.id.viewSwitcher);

        Animation slid_in_left = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.slide_in_left);
        Animation slid_out_right = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.slide_out_right);
        viewSwitcher.setInAnimation(slid_in_left);
        viewSwitcher.setOutAnimation(slid_out_right);
        setUpEventSpecs(dialogView);
    }

    private void animateDialog(View dialogView, boolean open) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        if (open) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(500);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                    dismiss();
                }
            });
            anim.setDuration(500);
            anim.start();
        }
    }

    public static ReportDialog newInstance(Context context, int cx, int cy) {
        ReportDialog dialog = new ReportDialog(context, R.style.MyAlertDialogStyle);
        dialog.cx = cx;
        dialog.cy = cy;
        return dialog;
    }

    private void setupRecyclerView(View dialogView) {
        recyclerView = dialogView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewAdapter = new ReportRecyclerViewAdapter(getContext(), Config.listItems);
        recyclerViewAdapter.setClickListener(new ReportRecyclerViewAdapter.OnClickListener() {
            @Override
            public void setItem(String item) {
                showNextViewSwitcher(item);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void showNextViewSwitcher(String item) {
        mEventype = item;
        if (viewSwitcher != null) {
            viewSwitcher.showNext();
            mTypeTextView.setText(mEventype);
            mEventTypeImg.setImageDrawable(ContextCompat.getDrawable(getContext(), Config.trafficMap.get(mEventype)));
        }
    }

    private void setUpEventSpecs(final View dialogView) {
        mImageCamera = (ImageView) dialogView.findViewById(R.id.event_camera_img);
        mBackButton = (Button) dialogView.findViewById(R.id.event_back_button);
        mSendButton = (Button) dialogView.findViewById(R.id.event_send_button);
        mCommentEditText = (EditText) dialogView.findViewById(R.id.event_comment);
        mEventTypeImg = (ImageView) dialogView.findViewById(R.id.event_type_img);
        mTypeTextView = (TextView) dialogView.findViewById(R.id.event_type);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogCallBack.onSubmit(mCommentEditText.getText().toString(), mEventype);
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.showPrevious();
            }
        });

        mImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogCallBack.StartCamera();
            }
        });

    }
    public void updateImage(Bitmap bitmap) {
        mImageCamera.setImageBitmap(bitmap);
    }

    public void setVocieInfor(String event_type, String prefillText) {
        mEventype = event_type;
        mPrefillText = prefillText;

    }




    @Override
    public void onStart() {
        super.onStart();
        if (mEventype != null) {
            showNextViewSwitcher(mEventype);
        }
        if (mPrefillText != null) {
            mCommentEditText.setText(mPrefillText);
        }
    }



}
