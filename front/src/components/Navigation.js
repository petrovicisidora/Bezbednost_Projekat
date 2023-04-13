import React, { Component } from 'react'
import { bindActionCreators } from "redux";
import { Link, withRouter } from "react-router-dom";
import { connect } from "react-redux";
import MenuState from "../constants/MenuState";

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import SendIcon from '@material-ui/icons/Send';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import Collapse from '@material-ui/core/Collapse';
import { Drawer } from "@material-ui/core";

class Navigation extends Component {

    constructor(props) {
        super(props);

        this.state = {

            submenu: {
                example: false
            }
        };
    }

    getNavigationClass() {

        if (this.props.menu.state === MenuState.SHORT) {
            return 'navigation-content-container short';
        }
        else {
            return 'navigation-content-container'
        }
    }

    isCurrentPath(path) {

        return this.props.history.location.pathname == path;
    }

    toggleSubmenu(key) {

        let submenu = this.state.submenu;

        submenu[key] = !submenu[key];

        this.setState({
            submenu: submenu
        });
    }

    render() {

        return (
            <Drawer variant="permanent" id='navigation'>

                <div className={this.getNavigationClass()}>
                    <div className='logo-container'>
                        <div className='title'>
                            <h2>Security</h2>
                        </div>
                    </div>

                    
                    {
                        this.props.auth.user && this.props.auth.user.role != 'ADMIN' &&
                        <React.Fragment>
                            <List component="nav">
                                <Link to={'/alarms'} className={this.isCurrentPath('/alarms') ? 'navigation-link active' : 'navigation-link'} >
                                    <ListItem className='navigation-item'>
                                        <ListItemText inset primary='Alarms' className='navigation-text' />
                                    </ListItem>
                                </Link>
                            </List>
                        </React.Fragment>
                    }

                    {
                        this.props.auth.user && this.props.auth.user.role == 'ADMIN' &&
                        <React.Fragment>
                            <List component="nav">
                                <Link to={'/alarms'} className={this.isCurrentPath('/alarms') ? 'navigation-link active' : 'navigation-link'} >
                                    <ListItem className='navigation-item'>
                                        <ListItemText inset primary='Alarms' className='navigation-text' />
                                    </ListItem>
                                </Link>
                            </List>
                            <List component="nav">
                                <Link to={'/users'} className={this.isCurrentPath('/users') ? 'navigation-link active' : 'navigation-link'} >
                                    <ListItem className='navigation-item'>
                                        <ListItemText inset primary='Users' className='navigation-text' />
                                    </ListItem>
                                </Link>
                            </List>
                            <List component="nav">
                                <Link to={'/certs'} className={this.isCurrentPath('/certs') ? 'navigation-link active' : 'navigation-link'} >
                                    <ListItem className='navigation-item'>
                                        <ListItemText inset primary='Certs' className='navigation-text' />
                                    </ListItem>
                                </Link>
                            </List>
                        </React.Fragment>
                    }

                </div>
            </Drawer>
        );
    }
}

function mapDispatchToProps(dispatch) {
    return bindActionCreators({}, dispatch);
}

function mapStateToProps({ menuReducers, authReducers }) {
    return { menu: menuReducers, auth: authReducers };
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Navigation));