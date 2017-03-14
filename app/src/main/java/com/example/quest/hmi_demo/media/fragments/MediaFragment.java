package com.example.quest.hmi_demo.media.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;
import com.example.quest.hmi_demo.media.adapters.HomeListRecyclerAdapter;
import com.example.quest.hmi_demo.media.const_class.Media_constants;
import com.example.quest.hmi_demo.media.supporting_classes.DividerItemDecoration;
import com.example.quest.hmi_demo.media.bean_classes.MediaPlayerSingleton;
import com.example.quest.hmi_demo.media.supporting_classes.MyBounceInterpolator;
import com.example.quest.hmi_demo.media.supporting_classes.Utilities;
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description: The class SongService is used for saving song list from the device in Media module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class MediaFragment extends Fragment implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener, LoaderManager.LoaderCallbacks<Cursor>, mClickListener, MainActivityFragmentA.MediaSelectedListener {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private ArrayList songs = new ArrayList();
    private ImageView coverArtImg;
    private TextView title, artistName, trackName, songCurrentDurationLabel, songTotalDurationLabel, repeatText, shuffleText;
    private ImageButton btnPlay, btnForward, btnBackward, btnNext, btnPrevious, btnPlaylist, btnRepeat, btnShuffle;
    private LinearLayout playerComponents;

    private SeekBar songProgressBar;
    private Context context;
    private RecyclerView recyclerView;
    private HomeListRecyclerAdapter recycleAdapter;
    private int tappedIndex, scrollPosition;
    private final int screenHeight = getScreenHeight();
    private final int height = ((screenHeight * 4) / 5);
    private final int dhl = height / 4;
    // Media Player
    //private MediaPlayer mp;
    private MediaPlayerSingleton mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private Media_constants media_constants = new Media_constants();

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------


    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public MediaFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.media_homefragment, container, false);
        title = (TextView) view.findViewById(R.id.mediatext);
        artistName = (TextView) view.findViewById(R.id.artisttext);
        trackName = (TextView) view.findViewById(R.id.trackname);
        coverArtImg = (ImageView) view.findViewById(R.id.coverart);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnForward = (ImageButton) view.findViewById(R.id.btnForward);
        btnBackward = (ImageButton) view.findViewById(R.id.btnBackward);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) view.findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) view.findViewById(R.id.library);
        btnRepeat = (ImageButton) view.findViewById(R.id.repeat);
        btnShuffle = (ImageButton) view.findViewById(R.id.shuffle);
        songProgressBar = (SeekBar) view.findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) view.findViewById(R.id.songTotalDurationLabel);
        repeatText = (TextView) view.findViewById(R.id.myRepeatText);
        shuffleText = (TextView) view.findViewById(R.id.myShuffleText);
        playerComponents = (LinearLayout) view.findViewById(R.id.playercomponents);
        recyclerView = (RecyclerView) view.findViewById(R.id.songlist);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(null);
        recyclerView.addItemDecoration(dividerItemDecoration);

        utils = new Utilities();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this);

        context = container.getContext();
        //startActivity();
        if (isReadPhoneStateAllowed()) {
            startActivity();

        } else {
            requestPhoneStatePermission();
        }
        return view;
    }

    public void startActivity() {
        if (mp == null) {
            // Mediaplayer
            recycleAdapter = new HomeListRecyclerAdapter(getActivity(), songs);
            recycleAdapter.setClickListener(this);
            recyclerView.setAdapter(recycleAdapter);
            getLoaderManager().initLoader(1, null, this);
            new SongAsyncLoader().execute();
        }
        if(mp.getArgumentStatus())
        {
            MediaPlayerSingleton mediaSong = MediaPlayerSingleton.getInstance();
            if(mediaSong!= null)
            {
                String title = mediaSong.getSongName();
                String artist = mediaSong.getArtistName();
                String songPath = mediaSong.getSongpath();
                String albumPath = mediaSong.getAlbumPath();
                System.out.println("MediaFragment.startActivity() title = " + title);
                System.out.println("MediaFragment.startActivity() artist = " + artist);
                System.out.println("MediaFragment.startActivity() songPath = " + songPath);
                System.out.println("MediaFragment.startActivity() albumPath = " + albumPath);
                ArrayList details = new ArrayList();
                details.add(0,0);
                details.add(1,title);
                details.add(2,artist);
                details.add(3, songPath);
                details.add(4, albumPath);
                mp.setArguments(false);
                playSongFromListTap(details);
            }


        }
        title.setTextSize(15);
        artistName.setTextSize(10);
        trackName.setTextSize(8);
        ViewGroup.LayoutParams layoutParams = coverArtImg.getLayoutParams();
        layoutParams.height = 100;
        layoutParams.height = 100;
        coverArtImg.setLayoutParams(layoutParams);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        mp.setPlayStatus("pause");
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.media_btn_play);
                    }
                } else {
                    // Resume song
                    if (mp != null) {
                        mp.start();
                        mp.setPlayStatus("play");
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.media_btn_pause);
                        updateProgressBar();
                    }
                }

            }
        });
        /*
         * Forward button click event
         * Forwards song specified seconds
         **/
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mp.getDuration()) {
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime >= 0) {
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (songs.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songs.size() - 1);
                    currentSongIndex = songs.size() - 1;
                }

            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Animation myAnim;
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getActivity().getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.media_btn_repeat);
                    repeatText.setText("Off");
                    myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.media_bounce);
                    myAnim.setInterpolator(interpolator);
                    btnRepeat.startAnimation(myAnim);
                    repeatText.startAnimation(myAnim);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.media_btn_repeat_focused);
                    repeatText.setText("On");
                    btnShuffle.setImageResource(R.drawable.media_btn_shuffle);
                    shuffleText.setText("Off");
                    myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.media_bounce);
                    myAnim.setInterpolator(interpolator);
                    btnRepeat.startAnimation(myAnim);
                    repeatText.startAnimation(myAnim);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Animation myAnim;
                //MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getActivity().getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.media_btn_shuffle);
                    shuffleText.setText("Off");
                    myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.media_bounce);
                    myAnim.setInterpolator(interpolator);
                    btnShuffle.startAnimation(myAnim);
                    shuffleText.startAnimation(myAnim);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.media_btn_shuffle_focused);
                    shuffleText.setText("On");
                    // btnRepeat.setImageResource(R.drawable.media_btn_repeat);
                    repeatText.setText("Off");
                    myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.media_bounce);
                    myAnim.setInterpolator(interpolator);
                    btnShuffle.startAnimation(myAnim);
                    shuffleText.startAnimation(myAnim);
                }
            }
        });
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlayListFragment playlist = new PlayListFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm != null) {

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.fl_main_a, playlist);
                    ft.commit();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity();
            } else {
                //Displaying another toast if permission is not granted

            }
        }
    }

    private boolean isReadPhoneStateAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestPhoneStatePermission() {
        //And finally ask for the permission
        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        startActivity();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {
        String albumUri = "";
        // Play song
        try {
            currentSongIndex = songIndex;
            ArrayList currentSong = new ArrayList();
            currentSong = (ArrayList) songs.get(songIndex);
            mp.reset();
            mp.setDataSource((String) currentSong.get(3));
            mp.prepare();
            mp.start();
            // Displaying Song title and Artist name
            artistName.setText((String) currentSong.get(2));
            trackName.setText((String) currentSong.get(1));


            ContentResolver musicResolver = context.getContentResolver();
            Cursor musicCursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID + "=?",
                    new String[]{(String) currentSong.get(6)},
                    null);

            if (musicCursor.moveToFirst()) {
                albumUri = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
            Drawable img = Drawable.createFromPath(albumUri);
            if (img == null) {
                coverArtImg.setImageResource(R.drawable.media_defaultcoverart);
            } else {
                coverArtImg.setImageDrawable(img);
            }
            mp.setSongName((String) currentSong.get(2));
            mp.setArtistName((String) currentSong.get(1));
            mp.setAlbumPath(albumUri);
            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.media_btn_pause);
            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to play a song
     *
     * @param list- Array List
     */
    public void playSongFromListTap(ArrayList list) {
        // Play song
        try {
            mp.reset();
            String title = (String) list.get(1);
            String artist = (String) list.get(2);
            String songPath = (String) list.get(3);
            String albumpath = (String) list.get(4);

            System.out.println("MediaFragment.playSongFromListTap() ->Title = "+title);
            System.out.println("MediaFragment.playSongFromListTap() ->artist = "+artist);
            System.out.println("MediaFragment.playSongFromListTap() ->songPath = "+songPath);
            System.out.println("MediaFragment.playSongFromListTap() ->albumpath = "+albumpath);

            mp.setDataSource(songPath);
            mp.prepare();
            mp.start();
            // Displaying Song title and Artist name
            artistName.setText(title);
            trackName.setText(artist);
            Drawable img = Drawable.createFromPath(albumpath);
            if (img == null) {
                coverArtImg.setImageResource(R.drawable.media_defaultcoverart);
            } else {
                coverArtImg.setImageDrawable(img);
            }
            mp.setSongName(title);
            mp.setArtistName(artist);
            mp.setAlbumPath(albumpath);
            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.media_btn_pause);
            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songs.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (songs.size() - 1)) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mp.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setScrollY(scrollPosition);
        if (mp != null) {
            if (mp.isPlaying()) {

                btnPlay.setImageResource(R.drawable.media_btn_pause);
                updateProgressBar();
                artistName.setText(mp.getSongName());
                trackName.setText(mp.getArtistName());
                Drawable img = Drawable.createFromPath(mp.getAlbumPath());
                if (img == null) {
                    coverArtImg.setImageResource(R.drawable.media_defaultcoverart);
                } else {
                    coverArtImg.setImageDrawable(img);
                }
            }
            else
            {
               String status = mp.getPlayStatus();
                switch (status)
                {
                    case "play":
                        break;
                    case "pause": {
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.media_btn_play);
                        artistName.setText(mp.getSongName());
                        trackName.setText(mp.getArtistName());
                        Drawable img = Drawable.createFromPath(mp.getAlbumPath());
                        if (img == null) {
                            coverArtImg.setImageResource(R.drawable.media_defaultcoverart);
                        } else {
                            coverArtImg.setImageDrawable(img);
                        }
                        long totalDuration = mp.getDuration();
                        long currentDuration = mp.getCurrentPosition();

                        // Displaying Total Duration time
                        songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
                        // Displaying time completed playing
                        songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));
                        int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                        //Log.d("Progress", ""+progress);
                        songProgressBar.setProgress(progress);
                        mp.pause();
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollPosition = recyclerView.getScrollY();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // getArguments().clear();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Mediaplayer
        mp = MediaPlayerSingleton.getInstance();
        mp.setOnCompletionListener(this);
        CursorLoader cursorLoader = null;
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        recycleAdapter = new HomeListRecyclerAdapter(getActivity(), songs);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setClickListener(this);
        // Toast.makeText(getActivity(), "Size is "+recycleAdapter.songList.size(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onClick(View view, int position, Object object) {

        tappedIndex = position;
        playSong(position);
    }

    @Override
    public void mediaSelected(String state) {

        switch (state) {
            case "min": {
                /*songCurrentDurationLabel.setVisibility(View.INVISIBLE);
                songTotalDurationLabel.setVisibility(View.INVISIBLE);
                playerComponents.setVisibility(View.INVISIBLE);
                btnPlaylist.setVisibility(View.INVISIBLE);
                btnRepeat.setVisibility(View.INVISIBLE);
                repeatText.setVisibility(View.INVISIBLE);
                btnShuffle.setVisibility(View.INVISIBLE);
                shuffleText.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);*/
                /*ScaleAnimation animation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                title.startAnimation(animation);*/

                title.setTextSize(10);
                artistName.setTextSize(8);
                trackName.setTextSize(8);

               /* final float startSize = 15; // Size in pixels
                final float endSize = 10;
                final int animationDuration = 600; // Animation duration in ms

                ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
                animator.setDuration(animationDuration);

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedValue = (float) valueAnimator.getAnimatedValue();
                        title.setTextSize(animatedValue);
                    }
                });

                animator.start();*/

                ViewGroup.LayoutParams layoutParams = coverArtImg.getLayoutParams();
                layoutParams.height = 100;
                layoutParams.height = 100;
                coverArtImg.setLayoutParams(layoutParams);

                break;
            }
            case "max": {
                songCurrentDurationLabel.setVisibility(View.VISIBLE);
                songTotalDurationLabel.setVisibility(View.VISIBLE);
                playerComponents.setVisibility(View.VISIBLE);
                btnPlaylist.setVisibility(View.VISIBLE);
                btnRepeat.setVisibility(View.VISIBLE);
                repeatText.setVisibility(View.VISIBLE);
                btnShuffle.setVisibility(View.VISIBLE);
                shuffleText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                title.setTextSize(25);
                artistName.setTextSize(15);
                trackName.setTextSize(10);
                //trackName.setPadding(0, 0, 5, 0);
                // artistName.setPadding(0, 0, 5, 0);
                ViewGroup.LayoutParams layoutParams = coverArtImg.getLayoutParams();
                layoutParams.height = 180;
                layoutParams.height = 180;
                coverArtImg.setLayoutParams(layoutParams);
                break;
            }
            case "default": {
                songCurrentDurationLabel.setVisibility(View.VISIBLE);
                songTotalDurationLabel.setVisibility(View.VISIBLE);
                playerComponents.setVisibility(View.VISIBLE);
                /*btnPlaylist.setVisibility(View.INVISIBLE);
                btnRepeat.setVisibility(View.INVISIBLE);
                repeatText.setVisibility(View.INVISIBLE);
                btnShuffle.setVisibility(View.INVISIBLE);
                shuffleText.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);*/


                /*Animation animZoomIn = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.media_zoom_in);

                //title.setTextSize(10);
                title.setAnimation(animZoomIn);
                artistName.setAnimation(animZoomIn);
                trackName.setAnimation(animZoomIn);*/
                title.setTextSize(15);
                artistName.setTextSize(10);
                trackName.setTextSize(8);
                ViewGroup.LayoutParams layoutParams1 = coverArtImg.getLayoutParams();
                layoutParams1.height = 100;
                layoutParams1.height = 100;
                coverArtImg.setLayoutParams(layoutParams1);

                break;
            }
            default: {
                songCurrentDurationLabel.setVisibility(View.VISIBLE);
                songTotalDurationLabel.setVisibility(View.VISIBLE);
                playerComponents.setVisibility(View.VISIBLE);
                btnPlaylist.setVisibility(View.INVISIBLE);
                btnRepeat.setVisibility(View.INVISIBLE);
                repeatText.setVisibility(View.INVISIBLE);
                btnShuffle.setVisibility(View.INVISIBLE);
                shuffleText.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                title.setTextSize(15);
                artistName.setTextSize(10);
                trackName.setTextSize(8);
                ViewGroup.LayoutParams layoutParams = coverArtImg.getLayoutParams();
                layoutParams.height = 100;
                layoutParams.height = 100;
                coverArtImg.setLayoutParams(layoutParams);
                break;
            }
        }


    }

    public class SongAsyncLoader extends AsyncTask<Void, List, List> {

        Context mContext;
        int count = 0;

        @Override
        protected List doInBackground(Void... params) {
            ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

            String[] projection = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM_ID
            };

            Cursor cursor = resolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    MediaStore.Audio.Media.TITLE + " ASC");

            songs = new ArrayList();
            while (cursor.moveToNext()) {

                ArrayList details = new ArrayList();
                details.add(cursor.getString(0));
                details.add(cursor.getString(1));
                details.add(cursor.getString(2));
                details.add(cursor.getString(3));
                details.add(cursor.getString(4));
                details.add(cursor.getString(5));
                details.add(cursor.getString(6));

                songs.add(details);
                if (count == 10) {
                    count = 0;
                    publishProgress(songs);
                }
                count++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(List... values) {
            super.onProgressUpdate(values);
            recycleAdapter.swapCursor(songs);
            mp.setSongList(songs);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}