package se.greatbrain.sats.ui;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.data.ActivityWrapper;
import se.greatbrain.sats.R;
import se.greatbrain.sats.event.ClassDetailEvent;
import se.greatbrain.sats.util.DateUtil;

public class ClassDetailFragment extends Fragment implements YouTubeThumbnailView
        .OnInitializedListener
{
    private ActivityWrapper wrapper;
    private String videoId;

    private final static String API_KEY = "AIzaSyBW0jMIhMr20zsk6DuZ5pAv6aTKfc2fGTE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_class_detail, container, false);

        TextView className = (TextView) view.findViewById(R.id.class_detail_class_name);
        TextView classDuration = (TextView) view.findViewById(R.id.class_detail_class_duration);
        TextView classInfo = (TextView) view.findViewById(R.id.class_detail_class_info);
        TextView availableSpots = (TextView) view.findViewById(R.id
                .class_detail_deltagare_max_answer);
        TextView queuePosition = (TextView) view.findViewById(R.id.class_detail_queue_position);
        TextView centerName = (TextView) view.findViewById(R.id.class_detail_center_answer);
        TextView classDate = (TextView) view.findViewById(R.id.class_detail_date_answer);
        TextView instructorName = (TextView) view.findViewById(R.id.class_detail_instructor_answer);

        className.setText(wrapper.trainingActivity.getClassType().getName());
        classDuration.setText(String.valueOf(wrapper.trainingActivity.getDurationInMinutes()) +
                "min");
        classInfo.setText(wrapper.trainingActivity.getClassType().getDescription());
        availableSpots.setText(wrapper.trainingActivity.getBooking().getSatsClass()
                .getBookedPersonsCount() + "/" + wrapper.trainingActivity.getBooking()
                .getSatsClass().getMaxPersonsCount());
        queuePosition.setText(String.valueOf(wrapper.trainingActivity.getBooking()
                .getPositionInQueue()));
        centerName.setText(wrapper.trainingActivity.getCenter().getName());
        classDate.setText(DateUtil.getListTitlePlanned(
                wrapper.trainingActivity.getBooking().getSatsClass().getStartTime()));
        instructorName.setText(
                wrapper.trainingActivity.getBooking().getSatsClass().getInstructorId());

        setUpProgressBar(view);
        setUpYoutubeThumbNail(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    public void onEventMainThread(ClassDetailEvent event)
    {
        wrapper = event.getActivityWrapper();
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
            YouTubeThumbnailLoader youTubeThumbnailLoader)
    {
        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader
                .OnThumbnailLoadedListener()
        {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s)
            {
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                    YouTubeThumbnailLoader.ErrorReason errorReason)
            {
            }
        });

        youTubeThumbnailLoader.setVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
            YouTubeInitializationResult youTubeInitializationResult)
    {}

    private void setUpProgressBar(View view)
    {
        ProgressBar condition = (ProgressBar) view.findViewById(R.id
                .class_detail_condition_progress_bar);
        ProgressBar strength = (ProgressBar) view.findViewById(R.id
                .class_detail_strength_progress_bar);
        ProgressBar flexibility = (ProgressBar) view.findViewById(R.id
                .class_detail_flexibility_progress_bar);
        ProgressBar balance = (ProgressBar) view.findViewById(R.id
                .class_detail_balance_progress_bar);
        ProgressBar agility = (ProgressBar) view.findViewById(R.id
                .class_detail_agility_progress_bar);

        int conditionValue = wrapper.trainingActivity.getClassType().getProfile().get(0).getValue();
        int strengthValue = wrapper.trainingActivity.getClassType().getProfile().get(1).getValue();
        int flexibilityValue = wrapper.trainingActivity.getClassType().getProfile().get(2)
                .getValue();
        int balanceValue = wrapper.trainingActivity.getClassType().getProfile().get(3).getValue();
        int agilityValue = wrapper.trainingActivity.getClassType().getProfile().get(4).getValue();

        condition.setProgress(conditionValue);
        strength.setProgress(strengthValue);
        flexibility.setProgress(flexibilityValue);
        balance.setProgress(balanceValue);
        agility.setProgress(agilityValue);
    }


    private void setUpYoutubeThumbNail(View view)
    {
        YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id
                .class_detail_youtube_video);
        thumbnail.initialize(API_KEY, this);

        final String videoUrl = wrapper.trainingActivity.getClassType().getVideoUrl();
        int questionMarkPosition = videoUrl.indexOf("?");
        videoId = videoUrl.substring(questionMarkPosition - 11, questionMarkPosition);

        Log.d("Url", videoUrl);
        Log.d("VideoId", videoId);

        thumbnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)));

            }
        });
    }
}
