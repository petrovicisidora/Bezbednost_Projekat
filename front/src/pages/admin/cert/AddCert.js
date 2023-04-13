import React from 'react'
import { bindActionCreators } from "redux";
import * as Actions from "../../../actions/Actions";
import { withRouter } from "react-router-dom";
import connect from "react-redux/es/connect/connect";
import Grid from '@material-ui/core/Grid';
import { Paper } from "@material-ui/core";
import strings from "../../../localization";
import Validators from "../../../constants/ValidatorTypes";
import FormComponent from "../../../common/FormComponent";
import UserForm from "../../../components/forms/admin/user/UserForm";
import { addUser } from "../../../services/admin/UserAdminService";
import { addCert } from '../../../services/admin/CertAdminService';
import CertForm from '../../../components/forms/admin/cert/CertForm';

class AddCert extends FormComponent {

    validationList = {
        issuerName: [{ type: Validators.REQUIRED }],
        
        name: [{ type: Validators.REQUIRED }],
        
        startDate: [{ type: Validators.REQUIRED }],
        endDate: [{ type: Validators.REQUIRED }]
    };

    constructor(props) {
        super(props);

        this.state = {
            data: props.data ? props.data : {
                issuerName: this.props.user.email
            },
            errors: {}
        };

        this.props.changeFullScreen(false);

        this.submit = this.submit.bind(this);
    }

    submit() {

        if (!this.validate()) {
            return;
        }

        this.showDrawerLoader();

        addCert({
            ...this.state.data,
            serialNumber:'nista'
        }).then(response => {

            if (!response.ok) {
                this.props.onFinish(null);
                return;
            }

            this.props.onFinish(response.data.user);

            this.hideDrawerLoader();
        });
    }

    render() {

        return (
            <Grid id='page' item md={12}>

                <div className='header'>
                    <h1>Add cert</h1>
                </div>

                <Paper className='paper'>
                    <CertForm onChange={this.changeData} onSubmit={this.submit} user={this.props.user}
                        data={this.state.data} errors={this.state.errors} onCancel={this.props.onCancel} />
                </Paper>

            </Grid>

        );
    }
}


function mapDispatchToProps(dispatch) {
    return bindActionCreators({
        changeFullScreen: Actions.changeFullScreen
    }, dispatch);
}

function mapStateToProps({ menuReducers, siteDataReducers }) {
    return { menu: menuReducers, siteData: siteDataReducers };
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(AddCert));