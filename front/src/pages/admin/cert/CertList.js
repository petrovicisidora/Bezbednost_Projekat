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
import { getCerts, deleteCert } from '../../../services/admin/CertAdminService';
import AddCert from './AddCert';

class CertList extends TablePage {

    tableDescription = [

        { key: 'issuerName', label: 'Issuer name' },
        { key: 'name', label: 'Subject name' },
        { key: 'startDate', label: 'Start date' },
        { key: 'endDate', label: 'End date' },
        { key: 'serialNumber', label: 'Serial Number'},
        { key: 'certType', label: 'Cert Type' },
    ];

    constructor(props) {
        super(props);
    }

    fetchData() {

        this.setState({
            lockTable: true
        });

        getCerts({
            page: this.state.searchData.page - 1,
            perPage: this.state.searchData.perPage,
            term: this.state.searchData.search.toLowerCase()
        }).then(response => {

            if (!response.ok) {
                return;
            }

            if(this.props.user && this.props.user.role == 'ADMIN') {
                this.setState({
                    tableData: response.data.entities,
                    total: response.data.total,
                    lockTable: false
                });
                return
            } 

            console.log(response.data.entities)

            this.setState({
                tableData: response.data.entities.filter(x => x.issuerName === this.props.user.email),
                total: response.data.total,
                lockTable: false
            });

            
        });
    }

    componentDidMount() {
        this.fetchData();
    }

    getPageHeader() {
        return <h1>Certs</h1>;
    }

    renderAddContent() {
        //if(!this.props.user.role=='ENDENTITY'){
        return <AddCert onCancel={this.onCancel} onFinish={this.onFinish} user={this.props.user}/>//}
    }

    delete(item) {

        this.setState({
            lockTable: true
        });

        deleteCert(item).then(response => {

            if (response && !response.ok) {
                this.onFinish(null);
                return;
            }


            this.onFinish(item);
            this.cancelDelete();

            this.setState({
                lockTable: false
            });
        });
    }


    renderRowMenu(index, item) {

        let ariaOwns = 'action-menu-' + index;

        return (
           <TableCell>
                 <IconButton
                    aria-owns={this.state.anchorEl ? ariaOwns : undefined}
                    aria-haspopup="true"
                    onClick={(event) => this.handleMenuClick(event, ariaOwns)}
                >
                    <MoreVert />
                </IconButton> 
                {
                    ariaOwns === this.state.ariaOwns &&
                    <Menu
                        id={ariaOwns}
                        anchorEl={this.state.anchorEl}
                        open={Boolean(this.state.anchorEl)}
                        onClose={() => this.handleMenuClose()}
                    >
                        {
                            !item[this.deletedField] &&
                            <MenuItem onClick={() => this.handleMenuDelete(item)}>
                                <ListItemIcon>
                                    <DeleteIcon />
                                </ListItemIcon>
                                <ListItemText inset primary={strings.table.delete} />
                            </MenuItem>
                        }

                    </Menu>
                }

            </TableCell>
        );
    }
}

function mapDispatchToProps(dispatch) {
    return bindActionCreators({
        changeFullScreen: Actions.changeFullScreen
    }, dispatch);
}

function mapStateToProps({ menuReducers, authReducers }) {
    return { menu: menuReducers, user: authReducers.user };
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(CertList));