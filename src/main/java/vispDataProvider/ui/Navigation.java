package vispDataProvider.ui;

import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Navigation {

    public List<NavEntry> getNavEntries() {
        return Arrays.asList(
                new NavEntry("/", "index", "Tasks"),
                new NavEntry("/newTask", "newTask", "Create Task"),
                new NavEntry("/topology", "topology", "Topology Upload"),
                new NavEntry("/configuration", "configuration", "Configuration"),
                new NavEntry("/about", "about", "About"));

    }

    private static class NavEntry {
        private final String url, viewName, userFriendlyName;

        NavEntry(String url, String viewName, String userFriendlyName) {
            this.url = url;
            this.viewName = viewName;
            this.userFriendlyName = userFriendlyName;
        }

        public String getUrl() {
            return url;
        }

        public String getViewName() {
            return viewName;
        }

        public String getUserFriendlyName() {
            return userFriendlyName;
        }
    }
}
