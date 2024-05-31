import React from "react";
import PropTypes from "prop-types";
import {
    Button,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow, TableSortLabel,
    TextField,
} from "@material-ui/core";
import {Link} from "react-router-dom";
import {withRouter} from "react-router";
import {TICKETS_TABLE_COLUMNS} from "../constants/tablesColumns";
import dateFormat from "dateformat";

class TicketsTable extends React.Component {
    constructor(props) {
        super(props);

        this.state ={
            orderBy:"id"
        }

    }

    handleChangePage = async (event, newPage) => {
        this.props.pageNumberCallback(newPage)
    };

    handleChangeRowsPerPage = async (event) => {
        this.props.pageSizeCallback(+event.target.value, 0)
    };

    handleSortRequest = async (cellId) => {
        const isAsc = this.state.orderBy === cellId && this.state.order === "asc";
        await this.setState({
            order: isAsc ? 'desc' : 'asc',
            orderBy: cellId
        });
       this.props.pageSortCallback(this.state.order, this.state.orderBy)
    };

    handleAction = (action, ticketId) => {

        const url = new URL('http://localhost:8080/ticket/' + ticketId + '/' + action.toLowerCase());
        fetch(url.toString(), {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
            }
        }).then(() => {
            this.props.actionCallback();
        })

    };

    render() {
        const {searchCallback, tickets} = this.props;
        const {
            handleChangePage,
            handleChangeRowsPerPage,
            handleAction
        } = this;

        return (
            <Paper>
                <TableContainer>
                    <TextField
                        onChange={searchCallback}
                        id="filled-full-width"
                        label="Search"
                        style={{margin: 5, width: "500px"}}
                        placeholder="Search for ticket"
                        margin="normal"
                        InputLabelProps={{
                            shrink: true,
                        }}
                    />
                    <Table>
                        <TableHead>
                            <TableRow>
                                {TICKETS_TABLE_COLUMNS.map((column) => (
                                    <TableCell align={column.align} key={column.id}>
                                        {column.id !== "action"  &&
                                        <TableSortLabel
                                            direction={this.state.orderBy === column.id ? this.state.order : 'acs'}
                                            onClick={ async () => {await this.handleSortRequest(column.id)}}>
                                            <b>{column.label}</b>
                                        </TableSortLabel>
                                        }
                                        {column.id === "action"  &&
                                            <b>{column.label}</b>
                                        }

                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {tickets.map((row, index) => {
                                    return (
                                        <TableRow hover role="checkbox" key={index}>
                                            {TICKETS_TABLE_COLUMNS.map((column) => {
                                                const value = row[column.id];
                                                if (column.id === "name") {
                                                    return (
                                                        <TableCell key={column.id}>
                                                            <Link to={`ticket-info/${row.id}`}>{value}</Link>
                                                        </TableCell>
                                                    );
                                                }
                                                if (column.id === "desiredResolutionDate") {
                                                    return <TableCell align="center" key={column.id}>
                                                        <TableCell>
                                                            {dateFormat(Date.parse(row.desiredResolutionDate), "dd/mm/yyyy")}
                                                        </TableCell>
                                                    </TableCell>
                                                }

                                                if (column.id === "action") {
                                                    if (row.state !== "DONE") {
                                                        return <TableCell align="center" key={column.id}>
                                                            {Object.keys(row.action).map((el) => {
                                                                return (
                                                                    <Button
                                                                        onClick={() => handleAction(row.action[el], row.id)}
                                                                        variant="contained"
                                                                        color="primary"
                                                                    >
                                                                        {row.action[el].split("_").join(" ")}
                                                                    </Button>
                                                                )
                                                            })}
                                                        </TableCell>;
                                                    }
                                                    if (row.state === "DONE") {
                                                        return <TableCell align="center" key={column.id}>
                                                            {Object.keys(row.action).map((el) => {
                                                                return (
                                                                    <Button
                                                                        component={Link}
                                                                        to={`/feedback/${row.id}`}
                                                                        variant="contained"
                                                                        color="primary"
                                                                    >
                                                                        {row.action[el].split("_").join(" ")}
                                                                    </Button>
                                                                )
                                                            })}
                                                        </TableCell>;
                                                    }
                                                } else {
                                                    return <TableCell key={column.id}>{value}</TableCell>;
                                                }
                                            })}
                                        </TableRow>
                                    );
                                })}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[3, 5, 10]}
                    component="div"
                    count={this.props.countCallback}
                    rowsPerPage={this.props.sizeCallback}
                    page={this.props.pageCallback}
                    onChangePage={handleChangePage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                />
            </Paper>
        );
    }
}

TicketsTable.propTypes = {
    searchCallback: PropTypes.func,
    actionCallback: PropTypes.func,
    tickets: PropTypes.array,
};

const TicketsTableWithRouter = withRouter(TicketsTable);
export default TicketsTableWithRouter;

