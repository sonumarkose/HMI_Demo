package com.example.quest.hmi_demo.message;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;

import java.util.ArrayList;


/**
 * Created by quest on 23/2/17.
 */
/*public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>*/
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Cursor mMsgCursor;
    private Cursor mCotactCursor;
    private Cursor mPhotoCursor;
    public String mContactAddress;
    ContentResolver cr;
    mClickListener clickListener;
    ArrayList<String> mAddressArray;

   private Context mContext;
    private Activity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            imageView = (ImageView) v.findViewById(R.id.icon);


            v.setOnClickListener(this);



        }


        @Override
        public void onClick(View view) {

            if (clickListener != null) {

                 String add=mAddressArray.get( getAdapterPosition());
                clickListener.onClick(view, getAdapterPosition(), add);

            }


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, Activity activity) {
       mContext = context;
        mActivity=activity;

        mAddressArray= new ArrayList<>();
        // prepare data
        Uri message = Uri.parse("content://sms/");
        cr = context.getContentResolver();


      mMsgCursor = cr.query(message, new String[]{"address", "body"},
               null, null, null);

       // context.getApplicationContext().startManagingCursor(mCursor); // *** need to call it properly
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_listcards_layout, parent, false);


        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        v.setLayoutParams(v.getLayoutParams());


        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        mMsgCursor.moveToPosition(position);
       mContactAddress = mMsgCursor.getString(mMsgCursor.getColumnIndexOrThrow("address"));

       // mContactAddress = mMsgCursor.getString(mMsgCursor.getColumnIndex("snippet"));

      //  holder.txtHeader.setText(m_ContactAddress);
        mAddressArray.add(mContactAddress);
       holder.txtFooter.setText(mMsgCursor.getString(mMsgCursor.getColumnIndexOrThrow("body")));


      //  mCursor.close();

        String contactName = getContactName(mContactAddress);

        if(contactName!=null){

            holder.txtHeader.setText(contactName);

        }
        else {
            holder.txtHeader.setText(mContactAddress);

        }

        String photoName = getPhotouri(mContactAddress);



        if(photoName!=null){
           holder.imageView.setImageURI(Uri.parse(photoName));
        }
        else {
            holder.imageView.setImageResource(R.drawable.ic_launcher);
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMsgCursor.getCount();
    }
    public void setClickListener(mClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }





    public String getContactName(String phoneNumber) {

       // ContentResolver cr = context.getContentResolver();
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


        //  Log.v("sfs","Kooi name "+contactName);
        return contactName;
    }


    /**get contact image
     *
     * **/
    public String getPhotouri(String phoneNumber) {


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




}