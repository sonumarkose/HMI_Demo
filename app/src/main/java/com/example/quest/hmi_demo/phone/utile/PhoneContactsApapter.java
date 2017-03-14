package com.example.quest.hmi_demo.phone.utile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.phone.interfaces.mClickListener;
import com.example.quest.hmi_demo.phone.model_classes.ContactPojo;

import java.util.List;

/**
 * Created by quest on 23/2/17.
 */
public class PhoneContactsApapter extends  RecyclerView.Adapter<PhoneContactsApapter.ContactViewHolder> {

    private List<ContactPojo> contactVOList;
    private Context mContext;
    private ContactPojo contactPojo;
    TextView lblNumber;
    String number;
    static mClickListener clickListener;
    public PhoneContactsApapter(List<ContactPojo> contactVOList, Context mContext){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.phone_contacts_adapter, null);


        /*lblNumber = (TextView) findViewById(R.id.lblNumber);
        String cid = getIntent().getStringExtra("cid");*//*

        // Read Contact number of specific contact with help of Content Resolver
        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[] { cid }, null);
        c.moveToFirst();
        number = c.getString(c
                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        // Display Contact Number into Label
        lblNumber.setText(number);




        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.phone);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });*/

        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }
    public void setClickListener(mClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactPojo contactPojo = contactVOList.get(position);
        holder.tvContactName.setText(contactPojo.getContactName());
        holder.tvPhoneNumber.setText(contactPojo.getContactNumber());

    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }



    public static class ContactViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}
