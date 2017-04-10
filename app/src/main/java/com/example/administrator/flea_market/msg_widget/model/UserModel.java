package com.example.administrator.flea_market.msg_widget.model;

import android.text.TextUtils;

import com.example.administrator.flea_market.msg_widget.model.QueryUserListener;
import com.example.administrator.flea_market.bean.MyUser;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/4/5.
 */
public class UserModel extends BaseModel {

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {}

    public MyUser getCurrentUser(){
        return BmobUser.getCurrentUser(getContext(), MyUser.class);
    }

    public void queryUsers(String username,int limit,final FindListener<MyUser> listener){
        BmobQuery<MyUser> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser(getContext());
            query.addWhereNotEqualTo("username",user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(getContext(), new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                if (list != null && list.size() > 0) {
                    listener.onSuccess(list);
                } else {
                    listener.onError(CODE_NULL, "查无此人");
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
            }
        });
    }

    /**查询用户信息
     * @param objectId
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener){
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(getContext(), new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                if(list!=null && list.size()>0){
                    listener.internalDone(list.get(0), null);
                }else{
                    listener.internalDone(new BmobException(000, "查无此人"));
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.internalDone(new BmobException(i, s));
            }
        });
    }

    /**更新用户资料和会话资料
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event,final UpdateCacheListener listener){
        final BmobIMConversation conversation=event.getConversation();
        final BmobIMUserInfo info =event.getFromUserInfo();
        final BmobIMMessage msg =event.getMessage();
        String username =info.getName();
        String title =conversation.getConversationTitle();
        Logger.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if(!username.equals(title)) {
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(MyUser s, BmobException e) {
                    if(e==null){
                        String name =s.getUsername();
                        String avatar = s.getAvator().getFileUrl(getContext());
                        Logger.i("query success："+name+","+avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if(!msg.isTransient()){
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    }else{
                        Logger.e(e);
                    }
                    listener.done(null);
                }
            });
        }else{
            listener.internalDone(null);
        }
    }

}

