package com.snake.ai.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.snake.ai.main;

public class HtmlLauncher extends GwtApplication {

    // USE THIS CODE FOR A FIXED SIZE APPLICATION
    //@Override
    //public GwtApplicationConfiguration getConfig() {
    //    return new GwtApplicationConfiguration();
    //}
    // END CODE FOR FIXED SIZE APPLICATION

    // UNCOMMENT THIS CODE FOR A RESIZABLE APPLICATION
    // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
    private static final float PADDING = 10f; //%
    private GwtApplicationConfiguration cfg;

    @Override
    public GwtApplicationConfiguration getConfig() {
        int h = Window.getClientHeight();
        int w = Window.getClientWidth();
        if (h / 1080f <= w / 2400f) {
            h = (int) (Window.getClientHeight() / 100f * (100 - PADDING));
            w = (int) (h / 1080f * 2400f);
        } else {
            w = (int) (Window.getClientWidth() / 100f * (100 - PADDING));
            h = (int) (w / 2400f * 1080f);
        }
        cfg = new GwtApplicationConfiguration(w, h);
        Window.enableScrolling(true);
        Window.setTitle("Snake Ai | eT4aa");
        Window.setMargin("0");
        Window.addResizeHandler(new ResizeListener());
        return cfg;
    }

    @Override
    public void onModuleLoad() {
        super.onModuleLoad();
        Window.enableScrolling(true);
    }


    class ResizeListener implements ResizeHandler {
        @Override
        public void onResize(ResizeEvent event) {
            int height = event.getHeight();
            int width = event.getWidth();
            if (height / 1080f <= width / 2400f) {
                height = (int) (event.getHeight() / 100f * (100 - PADDING));
                width = (int) (height / 1080f * 2400f);
            } else {
                width = (int) (event.getWidth() / 100f * (100 - PADDING));
                height = (int) (width / 2400f * 1080f);
            }
            getRootPanel().setWidth("" + width + "px");
            getRootPanel().setHeight("" + height + "px");
            getApplicationListener().resize(width, height);
            Gdx.graphics.setWindowedMode(width, height);
        }
    }
    // END OF CODE FOR RESIZABLE APPLICATION

    @Override
    public ApplicationListener createApplicationListener() {
        return new main(null);
    }
}