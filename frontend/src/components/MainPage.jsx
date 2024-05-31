import React from "react";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import {AppBar, Button, Tab, Tabs, Typography} from "@material-ui/core";
import {Link, Switch, Route} from "react-router-dom";
import {Redirect, withRouter} from "react-router";
import TicketInfoWithRouter from "./TicketInfo";


function a11yProps(index) {
    return {
        id: `full-width-tab-${index}`,
        "aria-controls": `full-width-tabpanel-${index}`,
    };
}

class MainPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            prop: 42,
            tabValue: 0,
            myTickets: [],
            allTickets: [],
            filteredTickets: [],
            pageNumber: 0,
            pageSize: 5,
            order: 'asc',
            orderBy: 'id',
            countTickets: 0,
            logout: false,
            userRole: ""
        };

    }

    componentDidMount() {
        this.performRequest();
    }

    performRequest = () => {
        if (localStorage.getItem('authorization') !== null) {
            const allTicketUrl = new URL('http://localhost:8080/ticket/all')
            allTicketUrl.searchParams.append("size", this.state.pageSize);
            allTicketUrl.searchParams.append("pageNumber", this.state.pageNumber)
            allTicketUrl.searchParams.append("sort", `${this.state.orderBy},${this.state.order}`)
            fetch(allTicketUrl.toString(), {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({
                        myTickets: json.content,
                        countAllTickets: json.totalElements
                    });
                });

            const roleTicketUrl = new URL('http://localhost:8080/ticket/role')
            roleTicketUrl.searchParams.append("size", this.state.pageSize);
            roleTicketUrl.searchParams.append("pageNumber", this.state.pageNumber)
            roleTicketUrl.searchParams.append("sort", `${this.state.orderBy},${this.state.order}`)
            fetch(roleTicketUrl.toString(), {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({
                        allTickets: json.content,
                        countMyTickets: json.totalElements
                    });
                });

            const userRoleUrl = new URL('http://localhost:8080/user')
            fetch(userRoleUrl.toString(), {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({
                        userRole: json
                    });
                });
        }
    }

    handleChangePageNUmber = async (number) => {
        await this.setState({
            pageNumber: number
        })
        this.performRequest()
    }

    handleChangePageSize = async (size, number) => {
        await this.setState({
            pageSize: size,
            pageNumber: number
        })
        this.performRequest()
    }

    handleLogout = () => {
        this.props.authCallback(false);
        localStorage.removeItem("authorization");
    };

    handleTabChange = (event, value) => {
        this.setState({
            tabValue: value,
            filteredTickets: []
        });
    };

    handleChangeSort = async (order, orderBy) => {
        await this.setState({
            order: order,
            orderBy: orderBy
        })
        this.performRequest()
    }

    handleSearchTicket = async (event) => {
        if (event.target.value === "") {
            await this.setState({
                filteredTickets: []
            })
        } else {
            const {tabValue, myTickets, allTickets} = this.state;
            if (tabValue === 0) {
                const filteredTickets = myTickets.filter((ticket) =>
                    ticket.name.toLowerCase().includes(event.target.value.toLowerCase())
                );

                await this.setState({
                    filteredTickets: filteredTickets
                });
            }
            if (tabValue === 1) {
                const filteredTickets = allTickets.filter((ticket) =>
                    ticket.name.toLowerCase().includes(event.target.value.toLowerCase())
                );
                await this.setState({
                    filteredTickets: filteredTickets
                });
            }
        }
    };

    render() {
        const {allTickets, filteredTickets, myTickets, tabValue, userRole} = this.state;
        const {path} = this.props.match;
        const {handleSearchTicket} = this;

        if (localStorage.getItem('authorization') === null) {
            return <Redirect to={`/`}/>;
        }

        return (
            <>
                <Switch>
                    <Route exact path={path}>
                        <div className="buttons-container">
                            {userRole !== "ENGINEER" &&
                            <Button
                                component={Link}
                                to="/create-ticket"
                                variant="contained"
                                color="primary"
                            >
                                Create Ticket
                            </Button>
                            }
                            <Typography variant="h3" color={"primary"}> HELP DESK</Typography>
                            <Button
                                component={Link}
                                to="/"
                                onClick={this.handleLogout}
                                variant="contained"
                                color="secondary"
                            >
                                Logout
                            </Button>
                        </div>
                        <div className="table-container">
                            <AppBar position="static">
                                <Tabs
                                    variant="fullWidth"
                                    onChange={this.handleTabChange}
                                    value={tabValue}
                                >
                                    <Tab label="My tickets" {...a11yProps(0)} />
                                    <Tab label="All tickets" {...a11yProps(1)} />
                                </Tabs>
                                <TabPanel value={tabValue} index={0}>
                                    <TicketsTable
                                        searchCallback={handleSearchTicket}
                                        actionCallback={this.performRequest}
                                        pageSizeCallback={this.handleChangePageSize}
                                        pageNumberCallback={this.handleChangePageNUmber}
                                        sizeCallback={this.state.pageSize}
                                        pageCallback={this.state.pageNumber}
                                        countCallback={this.state.countAllTickets}
                                        pageSortCallback={this.handleChangeSort}
                                        tickets={
                                            filteredTickets.length ? filteredTickets : myTickets
                                        }
                                    />
                                </TabPanel>
                                <TabPanel value={tabValue} index={1}>
                                    <TicketsTable
                                        searchCallback={handleSearchTicket}
                                        actionCallback={this.performRequest}
                                        pageSizeCallback={this.handleChangePageSize}
                                        pageNumberCallback={this.handleChangePageNUmber}
                                        sizeCallback={this.state.pageSize}
                                        pageCallback={this.state.pageNumber}
                                        countCallback={this.state.countMyTickets}
                                        pageSortCallback={this.handleChangeSort}
                                        tickets={
                                            filteredTickets.length ? filteredTickets : allTickets
                                        }
                                    />
                                </TabPanel>
                            </AppBar>
                        </div>
                    </Route>
                    <Route path={`${path}/:ticketId`}>
                        <TicketInfoWithRouter/>
                    </Route>
                </Switch>
            </>
        );
    }
}


const MainPageWithRouter = withRouter(MainPage);
export default MainPageWithRouter;
