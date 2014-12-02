package com.example.alexgomes_kevinmunar_dipenshah_sws901termproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PatientVideo extends Fragment {
    private MediaPlayer mediaPlayer;
    private SurfaceHolder videoHolder;
    private SurfaceView videoSurface;
    String videoAddress = "https://db.tt/F8adMnhK";

    @Override
    public void onStart() {
        super.onStart();
        videoSurface = (SurfaceView) getActivity().findViewById(R.id.surfView);
        videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                mediaPlayer.stop();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDisplay(videoHolder);
                    mediaPlayer.setDataSource(videoAddress);
                    mediaPlayer.prepare();
                    //mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_video, container, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
