import React from 'react'

import { bindActionCreators } from "redux";
import * as Actions from "../../actions/Actions";
import { withRouter } from "react-router-dom";
import connect from "react-redux/es/connect/connect";
import strings from "../../localization";
import Page from "../../common/Page";

import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Validators from "../../constants/ValidatorTypes";
import RegistrationForm from '../../components/forms/user/RegistrationForm';
import { addUser } from '../../services/admin/UserAdminService';



class Register extends Page {

    validationList = {
        email: [{ type: Validators.EMAIL }],
        firstName: [{ type: Validators.REQUIRED }],
        lastName: [{ type: Validators.REQUIRED }],
        password: [{ type: Validators.REQUIRED }]
    };

    constructor(props) {
        super(props);

        this.state = {
            data: {},
            errors: {},
            redirectUrl: props.location.state ? props.location.state.redirectUrl : '/'
        };

        this.props.changeFullScreen(true);

        this.keyPress = this.keyPress.bind(this);
    }

    componentDidMount() {

        if (this.props.auth.user) {
            this.props.history.push('/');
        }
    }

    keyPress(event) {

        if (event.key == 'Enter') {
            this.register()
        }
    }

    register() {

        if (!this.validate()) {
            return;
        }

        addUser(this.state.data).then(response => {

            if(!response || !response.ok) {
                return;
            }

            this.props.history.push({
                pathname: this.state.redirectUrl
            })
        });
    }

    render() {

        return (

            <div id='login'>
                <Grid item md={6}>
                    <Paper className='paper'>

                        <h1>Registration</h1>

                        <RegistrationForm onSubmit={() => this.register()} onChange={this.changeData}
                            keyPress={this.keyPress}
                            data={this.state.data} errors={this.state.errors} />
                    </Paper>
                </Grid>
            </div>
        );
    }
}

function mapDispatchToProps(dispatch) {
    return bindActionCreators({
        changeFullScreen: Actions.changeFullScreen,
        login: Actions.login
    }, dispatch);
}

function mapStateToProps({ menuReducers, authReducers }) {
    return { menu: menuReducers, auth: authReducers };
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Register));