package org.octavi.team;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout l = view.findViewById(R.id.root_con_lay);

        for (int i = 0; i < l.getChildCount(); i++) {
            if (l.getChildAt(i) instanceof TextView)
                ((TextView) l.getChildAt(i)).setShadowLayer(((TextView) l.getChildAt(i)).getTextSize(), 0f, 0f, Color.TRANSPARENT);
        }

        ((TextView) view.findViewById(R.id.role)).setText(this.getArguments().getString("TEAM_ROLE"));
        ((TextView) view.findViewById(R.id.styledName)).setText(this.getArguments().getString("TEAM_NAME"));
        ((TextView) view.findViewById(R.id.desc)).setText(this.getArguments().getString("TEAM_DESC"));
    }
}
