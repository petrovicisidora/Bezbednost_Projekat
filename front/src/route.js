import React from 'react';
import Home from './pages/Home';
import Error from "./pages/Error";
import Forbidden from "./pages/Forbidden";
import NotFound from "./pages/NotFound";

import { Route } from 'react-router-dom';
import { isUserLoggedIn } from "./base/OAuth";
import Login from "./pages/user/Login";
import UserList from "./pages/admin/users/UserList";
import Registration from './pages/user/Registration';
import CertList from './pages/admin/cert/CertList';
import AlarmList from './pages/admin/alarms/AlarmList';


let ROUTES = {
    // Home: {
    //     path: '/',
    //     component: <Home/>,
    //     auth: true
    // },
    Error: {
        path: '/error',
        component: <Error />,
        auth: false
    },
    Forbidden: {
        path: '/forbidden',
        component: <Forbidden />,
        auth: false
    },
    NotFound: {
        path: '/not-found',
        component: <NotFound />,
        auth: false
    },
    Login: {
        path: '/login',
        component: <Login />,
        auth: false
    },
    Register: {
        path: '/register',
        component: <Registration />,
        auth: false
    },
    UserList: {
        path: '/users',
        component: <UserList showFilter={false} />,
        auth: true
    },
    CertList: {
        path: '/certs',
        component: <CertList showFilter={false} />,
        auth: true
    },
    AlarmList: {
        path: '/alarms',
        component: <AlarmList showFilter={false} />,
        auth: true
    },
};

export default ROUTES;

function getRoute(path) {

    for (const [key, value] of Object.entries(ROUTES)) {

        if (value.path === path) {
            return value;
        }
    }

    return null;
}

export function checkPath(path) {

    let pathObject = getRoute(path);

    if (!pathObject) {
        return true;
    }

    if (pathObject.auth) {
        return !isUserLoggedIn();
    }

    return false;
}

export function getRoutes() {

    let result = [];

    for (const [key, value] of Object.entries(ROUTES)) {

        result.push(
            <Route key={'route-' + result.length} exact path={value.path} render={() => (
                value.component
            )} />
        )
    }

    return result;
}