//package io.github.barbosa.messagesview.library.adapters;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.github.barbosa.messagesview.library.models.Message;
//import io.github.barbosa.messagesview.library.views.ImageMessageMeListItem;
//import io.github.barbosa.messagesview.library.views.ImageMessageOthersListItem;
//import io.github.barbosa.messagesview.library.views.MessageListItem;
//import io.github.barbosa.messagesview.library.views.TextMessageMeListItem;
//import io.github.barbosa.messagesview.library.views.TextMessageOthersListItem;
//
//public class MessagesAdapter extends ArrayAdapter<Message> {
//
//    private static final int VIEW_TYPE_TEXT_OTHERS = 0;
//    private static final int VIEW_TYPE_TEXT_ME = 1;
//    private static final int VIEW_TYPE_IMAGE_OTHERS = 2;
//    private static final int VIEW_TYPE_IMAGE_ME = 3;
//
//    public MessagesAdapter(Context context) {
//        super(context, 0, new ArrayList<Message>());
//    }
//
//    public MessagesAdapter(Context context, List<Message> objects) {
//        super(context, 0, objects);
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 4;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Message message = getItem(position);
//
////        if (message.getFileUrl() != null) {
////            return message.isMine() ? VIEW_TYPE_IMAGE_ME : VIEW_TYPE_IMAGE_OTHERS;
////        }
//
//        return message.getSender().isMe() ? VIEW_TYPE_TEXT_ME : VIEW_TYPE_TEXT_OTHERS;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        MessageListItem view;
//
//        if (convertView == null) {
//            switch (getItemViewType(position)) {
//                case VIEW_TYPE_IMAGE_ME:
//                    view = new ImageMessageMeListItem(getContext()); break;
//                case VIEW_TYPE_IMAGE_OTHERS:
//                    view = new ImageMessageOthersListItem(getContext()); break;
//                case VIEW_TYPE_TEXT_ME:
//                    view = new TextMessageMeListItem(getContext()); break;
//                case VIEW_TYPE_TEXT_OTHERS:
//                default:
//                    view = new TextMessageOthersListItem(getContext()); break;
//            }
//        } else {
//            switch (getItemViewType(position)) {
//                case VIEW_TYPE_IMAGE_ME:
//                    view = (ImageMessageMeListItem)convertView; break;
//                case VIEW_TYPE_IMAGE_OTHERS:
//                    view = (ImageMessageOthersListItem)convertView; break;
//                case VIEW_TYPE_TEXT_ME:
//                    view = (TextMessageMeListItem)convertView; break;
//                case VIEW_TYPE_TEXT_OTHERS:
//                default:
//                    view = (TextMessageOthersListItem)convertView; break;
//            }
//        }
//
//        view.setMessage(getItem(position));
//
//        return view;
//    }
//}
