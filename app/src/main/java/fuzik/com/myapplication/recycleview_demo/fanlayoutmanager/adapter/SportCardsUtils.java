package fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.adapter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fuzik.com.myapplication.R;
import fuzik.com.myapplication.recycleview_demo.fanlayoutmanager.model.SportCardModel;

public class SportCardsUtils {

    public static Collection<SportCardModel> generateSportCards() {
        List<SportCardModel> sportCardModels = new ArrayList<>(5);

        {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Table tennis")
                    .withSportSubtitle("Woman's singles")
                    .withSportRound("Preliminaries")
                    .withImageResId(R.mipmap.pic_card_1)
                    .withTime("3:00")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.dark_orchid)
                    .build());

        }

        {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Shooting")
                    .withSportSubtitle("Woman's 10m air rifle")
                    .withSportRound("Qualification")
                    .withImageResId(R.mipmap.pic_card_2)
                    .withTime("2:30")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.mantis)
                    .build());

        }

        {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Rowing")
                    .withSportSubtitle("Men's single sculls")
                    .withSportRound("Heats")
                    .withImageResId(R.mipmap.pic_card_3)
                    .withTime("2:30")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.usc_gold)
                    .build());

        }

        {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Archery")
                    .withSportSubtitle("Men's team")
                    .withSportRound("Round of 16")
                    .withImageResId(R.mipmap.pic_card_5)
                    .withTime("3:00")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.portland_orange)
                    .build());

        }

        {
            sportCardModels.add(SportCardModel
                    .newBuilder()
                    .withSportTitle("Water polo")
                    .withSportSubtitle("Men’s tournament")
                    .withSportRound("Group A")
                    .withImageResId(R.mipmap.pic_card_4)
                    .withTime("3:00")
                    .withDayPart("PM")
                    .withBackgroundColorResId(R.color.dodger_blue)
                    .build());

        }
        return sportCardModels;
    }
}
