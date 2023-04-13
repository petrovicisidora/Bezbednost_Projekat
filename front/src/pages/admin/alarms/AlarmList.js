import React from 'react'
import TablePage from "../../../common/TablePage";
import { deleteUser, getUsers, restoreUser } from "../../../services/admin/UserAdminService";
import { bindActionCreators } from "redux";
import * as Actions from "../../../actions/Actions";
import { withRouter } from "react-router-dom";
import connect from "react-redux/es/connect/connect";
import strings from "../../../localization";
import { ListItemIcon, ListItemText, Menu, MenuItem, TableCell } from "@material-ui/core";
import IconButton from "@material-ui/core/IconButton";
import MoreVert from '@material-ui/icons/MoreVert';
import UndoIcon from '@material-ui/icons/Undo';
import DeleteIcon from '@material-ui/icons/Delete';
import { getAlarms } from '../../../services/admin/AlarmService';

class AlarmList extends TablePage {

    tableDescription = [
        { key: 'id', label: 'ID' },
        { key: 'date', label: 'Date' },
        { key: 'alarm', label: 'Alarm' },
        { key: 'agent', label: 'Agent' },
    ];

    constructor(props) {
        super(props);

        this.state.showActions = false;
    }

    fetchData() {

        this.setState({
            lockTable: true
        });

        getAlarms({
            page: this.state.searchData.page - 1,
            perPage: this.state.searchData.perPage,
            term: this.state.searchData.search.toLowerCase()
        }).then(response => {

            if (!response.ok) {
                return;
            }

            this.setState({
                tableData: response.data.entities,
                total: response.data.total,
                lockTable: false
            });
        });
    }

    componentDidMount() {
        this.fetchData();
    }

    getPageHeader() {
        return <h1>Alarms</h1>;
    }

    renderAddContent() {
        return ''
    }


}

function mapDispatchToProps(dispatch) {
    return bindActionCreators({
        changeFullScreen: Actions.changeFullScreen
    }, dispatch);
}

function mapStateToProps({ menuReducers }) {
    return { menu: menuReducers };
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(AlarmList));