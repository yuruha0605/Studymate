package edu.yonsei.Studymate.common;

public final class ApiUrls {
    private static final String API_BASE = "/api/study-mate";
    private static final String VIEW_BASE = "/study-mate";

    public static final class Auth {
        public static final String BASE = API_BASE + "/auth";
        public static final String LOGIN = BASE + "/login";
        public static final String SIGNUP = BASE + "/signup";
        public static final String LOGOUT = BASE + "/logout";
    }

    public static final class StudyGroup {
        public static final String BASE = API_BASE + "/studygroups";
        public static final String CREATE = BASE + "/create";
        public static final String LIST = BASE + "/list";
        public static final String DETAIL = BASE + "/{groupId}";
        public static final String JOIN = DETAIL + "/join";
        public static final String DELETE = BASE + "/delete/{groupId}";
        public static final String SEARCH = BASE + "/search";
    }

    public static final class Post {
        public static final String BASE = API_BASE + "/posts";
        public static final String CREATE = BASE + "/create";
        public static final String LIST = BASE + "/{groupId}/list";
        public static final String DETAIL = BASE + "/{postId}";
        public static final String UPDATE = DETAIL + "/update";
        public static final String DELETE = DETAIL + "/delete";
    }

    public static final class Subject {
        public static final String BASE = API_BASE + "/subjects";
        public static final String CREATE = BASE + "/create";
        public static final String DETAIL = BASE + "/{subjectId}";
        public static final String SEARCH = BASE + "/search";
    }

    public static final class View {
        public static final String LOGIN = VIEW_BASE + "/login";
        public static final String SIGNUP = VIEW_BASE + "/signup";
        public static final String MAIN = VIEW_BASE + "/main";
        public static final String STUDY_ROOM = VIEW_BASE + "/studygroups/{groupId}";
        public static final String SEARCH = VIEW_BASE + "/search";
        public static final String MY_CLASS = VIEW_BASE + "/view/myclass";
        public static final String STUDY_GROUPS = VIEW_BASE + "/view/study-groups";

    }

    public static final class Board {
        public static final String BASE = API_BASE + "/boards";
        public static final String CREATE = BASE + "/{studygroupId}/create";
        public static final String DETAIL = BASE + "/{studygroupId}";
    }

    public static final class Myclass {
        public static final String BASE = API_BASE + "/myclass";
        public static final String LIST = BASE + "/list";
        public static final String JOIN = BASE + "/{classId}/join";
        public static final String USER_CLASSES = BASE + "/user/{userId}";
    }


}
