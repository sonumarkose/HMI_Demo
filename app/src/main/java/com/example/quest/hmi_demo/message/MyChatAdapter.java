package com.example.quest.hmi_demo.message;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by quest on 3/3/17.
 */

public class MyChatAdapter  extends RecyclerView.Adapter<MyChatAdapter.ViewHolder> {

    private Cursor mMsgCursor;
    public String mContactAddress;
    ContentResolver cr;
    mClickListener clickListener;

    private Context mContext;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
      //  public ImageView imageView;

        public ViewHolder(View v) {
            super(v);

            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtHeader= (TextView) v.findViewById(R.id.firstLine);


            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            if (clickListener != null) clickListener.onClick(view, getAdapterPosition(),mContactAddress);

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)

    public MyChatAdapter(Context context, String address) {
        mContext = context;
        mContactAddress=address;
       // Uri message = Uri.parse("content://sms/");
        cr = context.getContentResolver();

        mMsgCursor = context.getContentResolver().query(Uri.parse("content://sms/")
                , null
                , "address = '" + mContactAddress + "'"
                , null
                , null);

        // context.getApplicationContext().startManagingCursor(mCursor); // *** need to call it properly
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_chat_layout, parent, false);


        // set the view's size, margins, paddings and layout parameters
        MyChatAdapter.ViewHolder vh = new MyChatAdapter.ViewHolder(v);
        v.setLayoutParams(v.getLayoutParams());


        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

     //   mMsgCursor.moveToPosition(position);
        mMsgCursor.moveToPosition(getItemCount()-position-1);


        String type =  mMsgCursor.getString(mMsgCursor.getColumnIndex("type"));


        if(type.equalsIgnoreCase("1")){

            holder.txtFooter.setGravity(Gravity.LEFT);
            holder.txtHeader.setGravity(Gravity.LEFT);
           holder.txtHeader.setBackgroundColor(Color.CYAN);
           holder.txtFooter.setBackgroundColor(Color.CYAN);

          /*  GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFF00FF00); // Changes this drawbale to use a single color instead of a gradient
            gd.setCornerRadius(5);
            gd.setStroke(1, 0xFF000000);

            holder.txtFooter.setBackgroundDrawable(gd);
*/
        }
        else  if(type.equalsIgnoreCase("2")){

            holder.txtFooter.setGravity(Gravity.RIGHT);
            holder.txtHeader.setGravity(Gravity.RIGHT);
           holder.txtHeader.setBackgroundColor(Color.GREEN);
           holder.txtFooter.setBackgroundColor(Color.GREEN);
        }


        holder.txtFooter.setText(mMsgCursor.getString(mMsgCursor.getColumnIndexOrThrow("body")));

      //  holder.txtHeader.setText(mMsgCursor.getString(mMsgCursor.getColumnIndex("date")));

        String date =  mMsgCursor.getString(mMsgCursor.getColumnIndex("date"));

        Long timestamp = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date finaldate = calendar.getTime();
        String smsDate = finaldate.toString();


        holder.txtHeader.setText(smsDate);



    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMsgCursor.getCount();
    }



    public void setClickListener(mClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }



/*


    public String getContactName(String phoneNumber) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        mCotactCursor = cr.query(uri,
                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        if (mCotactCursor == null) {
            return null;
        }
        String contactName = null;
        String uridsd = null;
        if (mCotactCursor.moveToFirst()) {
            contactName = mCotactCursor.getString(mCotactCursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        }

        return contactName;
    }


    */
/**get contact image
     *
     * **//*

    public  String getPhotouri(String phoneNumber) {


        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        mPhotoCursor = cr.query(uri,
                new String[] { ContactsContract.PhoneLookup.PHOTO_URI }, null, null, null);

        if (mPhotoCursor == null) {
            return null;
        }
        String contactName = null;

        if (mPhotoCursor.moveToFirst()) {
            contactName = mPhotoCursor.getString(mPhotoCursor
                    .getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

        }
        if (mPhotoCursor != null && !mPhotoCursor.isClosed()) {
            mPhotoCursor.close();
        }


        return contactName;
    }

*/




}
