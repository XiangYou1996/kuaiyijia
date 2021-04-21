package com.example.kuaiyijia.Utils;

/*
Author by: xy
Coding On 2021/3/9;
*/public class SomeMethods {
    //  由于handler不好处理了  所以不好封装
    /*public void deletePersoninfo(int position , Context context, ){
        // 弹出对话框询问是否真的删除
        CustomDialog dialog = new CustomDialog(context);

        dialog.setTitle("提示");
        dialog.setMessage("确定要删除该名员工吗？");
        dialog.setConfirm("确定",new CustomDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(CustomDialog dialog) {
                PersonListItem person = adapterLists.get(position);
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        // 删除employ 数据
                        int result1 = Database.deleteFromData("PUB_EMPLOYEE","EM_ID",person.getPerson_ID());
                        // 删除第二个表
                        int result2 = Database.deleteFromData("PUB_EMETYPE", "EM_ID",person.getPerson_ID());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        message.what = 1041;
                        bundle.putInt("result", result1&result2);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                };
                thread.start();
                thread.interrupt();
            }
        });
        dialog.setCancel("取消", new CustomDialog.IOnCancelListener() {
            @Override
            public void onCancel(CustomDialog dialog) {
                Toast.makeText(context,"取消删除~",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }*/
}
