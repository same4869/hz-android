package com.renren.wawa.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.renren.wawa.R;
import com.renren.wawa.utils.ScreenUtil;
import com.renren.wawa.utils.ViewUtil;

public class CommEditDialog extends Dialog implements View.OnClickListener {

    private View rootView;
    private TextView titleView;
    private TextView messageView;
    private OnClickListener rightListener, leftListener;
    private Button rightBtn, leftBtn;
    private String title, message;
    private boolean single = false;
    private boolean isContentHtml = false;
    private boolean leftPositive = false;
    private boolean rightPositive = true;
    private int customLayoutResId = 0;
    private View customLayout;
    private boolean bShow = true;
    private Context mContext;
    private FrameLayout commDialogRoot;

    public void setRightListener(OnClickListener rightListener) {
        this.rightListener = rightListener;
    }

    public void setLeftListener(OnClickListener leftListener) {
        this.leftListener = leftListener;
    }

    /**
     * constructor for classic dialog
     *
     * @param
     */
    public CommEditDialog(Context context, String title, String message, boolean single) {
        super(context, true, null);
        this.title = title;
        this.message = message;
        this.single = single;
        this.mContext = context;
    }

    public CommEditDialog(Context context, String title, int customLayoutResId, boolean single) {
        super(context, true, null);
        this.title = title;
        this.customLayoutResId = customLayoutResId;
        this.single = single;
        this.mContext = context;
    }

    public CommEditDialog(Context context, String title, String message, boolean isContentHtml, boolean single) {
        super(context, true, null);
        this.title = title;
        this.message = message;
        this.single = single;
        this.mContext = context;
        this.isContentHtml = isContentHtml;
    }

    /**
     * return customview, you should also override getSkinConfigName
     *
     * @return
     */

    protected View onCreateCustomView() {
        return null;
    }

    protected String getSkinConfigName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.comm_edit_dialog, null);
        setContentView(rootView);

        findViewById(R.id.comm_dialog_root).setOnClickListener(this);

        commDialogRoot = (FrameLayout) findViewById(R.id.comm_dialog_root);

        titleView = (TextView) findViewById(R.id.comm_dialog_title);
        messageView = (TextView) findViewById(R.id.comm_dialog_message);
        // messageView.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (customLayoutResId != 0) {
            customLayout = LayoutInflater.from(getContext()).inflate(customLayoutResId, null);
            FrameLayout customLayoutContainer = (FrameLayout) findViewById(R.id
                    .comm_custom_layout_container);
            customLayoutContainer.addView(customLayout);
            messageView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        if (message != null) {
            if (isContentHtml) {
                messageView.setText(Html.fromHtml(message));
            } else {
                messageView.setText(message);
            }
        }


        rightBtn = (Button) findViewById(R.id.comm_dialog_right_btn);
        rightBtn.setOnClickListener(this);
        leftBtn = (Button) findViewById(R.id.comm_dialog_left_btn);
        leftBtn.setOnClickListener(this);

        if (single) {
            leftBtn.setVisibility(View.GONE);
            findViewById(R.id.comm_divider_view).setVisibility(View.GONE);
        }

        applyLayoutParams();
        setLeftButtonPositive(leftPositive);
        setRightButtonPositive(rightPositive);
    }

    public FrameLayout getCustomLayoutContainer() {
        FrameLayout customLayoutContainer = (FrameLayout) findViewById(R.id
                .comm_custom_layout_container);
        return customLayoutContainer;
    }

    public void hideTitle() {
        titleView.setVisibility(View.GONE);
    }

    public void showTitle() {
        titleView.setVisibility(View.VISIBLE);
    }

    public void hideButtons() {
        findViewById(R.id.comm_buttons_layout).setVisibility(View.GONE);
    }

    public void showButtons() {
        findViewById(R.id.comm_buttons_layout).setVisibility(View.VISIBLE);
    }

    public void setTitleViewPadding(int left, int top, int right, int bottom) {
        titleView.setPadding(left, top, right, bottom);
    }

    public void hideMessageView() {
        messageView.setVisibility(View.GONE);
    }


    /**
     * @param text 要先调用dialog.show()才能用
     */
    public void setRightButtonText(String text) {
        rightBtn.setText(text);
    }

    public String getRightButtonText() {
        return rightBtn.getText().toString();
    }

    public void setLeftButtonText(String text) {
        leftBtn.setText(text);
    }

    public String getLeftButtonText() {
        return leftBtn.getText().toString();
    }

    public Button getLeftButton() {
        return leftBtn;
    }

    public void setDrawableTop(int resId) {
        messageView.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
    }

    public void setDrawableBottom(int resId) {
        messageView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resId);
    }

    public void setMessageGravity(int gravity) {
        messageView.setGravity(gravity);
    }

    public void showSingleButton(boolean single) {
        this.single = single;
        leftBtn.setVisibility(single ? View.GONE : View.VISIBLE);
        findViewById(R.id.comm_divider_view).setVisibility(single ? View.GONE : View.VISIBLE);
    }

    public View getCustomView() {
        return customLayout;
    }

    private void applyLayoutParams() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.width = (int) (ScreenUtil.getScreenHeight() * 0.9);
        } else {
            lp.width = (int) (ScreenUtil.getScreenWidth() * 0.9);
        }
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.comm_dialog_right_btn) {
            if (rightListener != null) {
                rightListener.onClick(this, 1);
            } else {
                dismiss();
            }
        } else if (id == R.id.comm_dialog_left_btn) {
            if (leftListener != null) {
                leftListener.onClick(this, 0);
            } else {
                dismiss();
            }
        }

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        rightListener = null;
        leftListener = null;
    }

    @SuppressWarnings("deprecation")
    public void setLeftButtonPositive(boolean positive) {
        if (positive) {
            leftBtn.setTextColor(mContext.getResources().getColor(R.color.button_text_normal_1));
            ViewUtil.setBackground(leftBtn, mContext.getResources().getDrawable(R.drawable
                    .comm_bg_btn));
        } else {
            leftBtn.setTextColor(mContext.getResources().getColor(R.color.button_bg_normal_1));
            ViewUtil.setBackground(leftBtn, mContext.getResources().getDrawable(R.drawable
                    .comm_bg_btn4));
        }
        leftPositive = positive;
    }

    @SuppressWarnings("deprecation")
    public void setRightButtonPositive(boolean positive) {
        if (positive) {
            rightBtn.setTextColor(mContext.getResources().getColor(R.color.button_text_normal_1));
            ViewUtil.setBackground(rightBtn, mContext.getResources().getDrawable(R.drawable
                    .comm_bg_btn));
        } else {
            rightBtn.setTextColor(mContext.getResources().getColor(R.color.button_bg_normal_1));
            ViewUtil.setBackground(rightBtn, mContext.getResources().getDrawable(R.drawable
                    .comm_bg_btn4));
        }
        rightPositive = positive;
    }

    public void dismiss(boolean anim) {
        if (anim) {
            dismiss();
        } else {
            super.dismiss();
        }
    }


}

