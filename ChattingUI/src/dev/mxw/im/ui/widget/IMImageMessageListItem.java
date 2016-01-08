package dev.mxw.im.ui.widget;

import android.content.Context;
import android.widget.TextView;
import dev.mxw.im.data.bean.IMMessage;
import dev.mxw.im.user.MyInfo;
import dev.mxw.im.user.OtherInfo;

public class IMImageMessageListItem extends IMMessageListItem {

    protected TextView imageNameTextView;

    public IMImageMessageListItem(Context context,MyInfo myinfo,OtherInfo otherinfo) {
        super(context, myinfo, otherinfo);
    }

    @Override
    public void setMessage(IMMessage message) {
        super.setMessage(message);
        if(imageNameTextView!=null){
            imageNameTextView.setText("");
        }
    }
}
