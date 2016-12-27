package com.github.xiaoma.sniper.container;

import com.github.xiaoma.sniper.core.extension.ExtensionLoader;

/**
 * Created by machunxiao on 16/12/26.
 */
public class Main {

    private static final ExtensionLoader<Container> loader = ExtensionLoader.getExtensionLoader(Container.class);

    public static void main(String[] args) {
        Container container = loader.getExtension("spring");
        container.start();
        Runtime.getRuntime().addShutdownHook(new Thread(container::stop, "container"));

    }
}
