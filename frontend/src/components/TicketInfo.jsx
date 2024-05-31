import React from "react";
import PropTypes from "prop-types";
import CommentsTable from "./CommentsTable";
import HistoryTable from "./HistoryTable";
import TabPanel from "./TabPanel";
import TicketCreationPageWithRouter from "./TicketCreationPage";
import {Link, Route, Switch} from "react-router-dom";
import {Redirect, withRouter} from "react-router";
import dateFormat from "dateformat";
import {
    Button,
    ButtonGroup,
    Paper,
    Tab,
    Tabs,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    Typography,
    TextField,
} from "@material-ui/core";

function a11yProps(index) {
    return {
        id: `full-width-tab-${index}`,
        "aria-controls": `full-width-tabpanel-${index}`,
    };
}

class TicketInfo extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            commentValue: "",
            tabValue: 0,
            ticketComments: [],
            ticketHistory: [],

            ticketData: {
                feedbackId: null,
                id: '',
                name: '',
                createdOn: '',
                category: '',
                state: '',
                urgency: '',
                desiredResolutionDate: '',
                ticketOwner: "",
                approver: "",
                assignee: "",
                attachment: "",
                description: "",
                action: []
            },
        };
    }

    performRequest() {
        const {ticketId} = this.props.match.params;
        const historyUrl = new URL('http://localhost:8080/history')
        historyUrl.searchParams.append("ticketId", ticketId);
        fetch(historyUrl.toString(), {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            }
        }).then(response => response.json())
            .then(json => {
                this.setState({
                    ticketHistory: json
                });
            });

        const commentsUrl = new URL('http://localhost:8080/comment')
        commentsUrl.searchParams.append("ticketId", ticketId);
        fetch(commentsUrl.toString(), {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            }
        }).then(response => response.json())
            .then(json => {
                this.setState({
                    ticketComments: json
                });
            });
    }

    componentDidMount() {
        this.performTicket()
    }

    performTicket() {
        if (localStorage.getItem('authorization') !== null) {
            const {ticketId} = this.props.match.params;

            fetch('http://localhost:8080/ticket/' + ticketId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({

                        ticketData: {
                            feedbackId: json.feedbackDto.id,
                            id: json.id,
                            name: json.name,
                            createdOn: json.createdOn,
                            state: json.state,
                            category: json.category,
                            urgency: json.urgency,
                            desiredResolutionDate: json.desiredResolutionDate,
                            ticketOwner: json.owner,
                            approver: json.approver,
                            assignee: json.assignee,
                            description: json.description,
                            attachment: json.attachment,
                            action: json.action
                        }
                    });
                });

            this.performRequest()
        }

    };

    handleTabChange = (event, value) => {
        this.setState({
            tabValue: value,
        });
    };

    handleEnterComment = (event) => {
        this.setState({
            commentValue: event.target.value,
        });
    };

    addComment = async () => {
        const commentUrl = new URL('http://localhost:8080/comment');
        commentUrl.searchParams.append("ticketId", this.state.ticketData.id);

        let response = await fetch(commentUrl.toString(), {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                text: this.state.commentValue
            })
        })
        if (response.ok) {
            this.performRequest()
            this.setState({
                commentValue: ''
            })
        }
    };

    handleSubmitTicket = () => {
        // set ticket status to 'submitted'
        const url = new URL('http://localhost:8080/ticket/state');
        url.searchParams.append("state", "NEW");
        url.searchParams.append("ticketId", this.state.ticketData.id);

        fetch(url.toString(), {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            },
        }).then(response => response.json())
    };

    handleEditTicket = () => {

    };

    handleDownloadAttachment = (id, name) => {
        const url = new URL('http://localhost:8080/attachment/' + id);
        const FileDownload = require("js-file-download");
        fetch(url.toString(), {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            },
        }).then(function (response) {
            return response.blob();
        }).then(function (blob) {
            FileDownload(blob, name);
        })
    };


    handleCancelTicket = () => {
        const url = new URL('http://localhost:8080/ticket/state');
        url.searchParams.append("state", "CANCELED");
        url.searchParams.append("ticketId", this.state.ticketData.id);

        fetch(url.toString(), {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            },
        }).then(response => response.json())
    };

    handleAction = (action, ticketId) => {
        const url = new URL('http://localhost:8080/ticket/' + ticketId + '/' + action.toLowerCase());
        let response = fetch(url.toString(), {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
            }
        }).then(() => {
            this.performTicket();
        })

    };

    render() {
        const {
            approver,
            id,
            name,
            category,
            state,
            urgency,
            ticketOwner,
            assignee,
            description,
            action,
            feedbackId
        } = this.state.ticketData;

        const createdOn = dateFormat(Date.parse(this.state.ticketData.createdOn), "dd/mm/yyyy");
        const desiredResolutionDate = dateFormat(Date.parse(this.state.ticketData.desiredResolutionDate), "dd/mm/yyyy");
        const attachment = this.state.ticketData.attachment;


        const {commentValue, tabValue, ticketComments, ticketHistory} =
            this.state;

        const {url} = this.props.match;

        const {handleDownloadAttachment, handleEditTicket, handleAction} = this;

        if (localStorage.getItem('authorization') === null) {
            return <Redirect to={`/`}/>;
        }

        return (
            <Switch>
                <Route exact path={url}>
                    <div className="ticket-data-container">
                        <div className={"ticket-data-container__back-button back-button"}>
                            <Button component={Link} to="/main-page" variant="contained">
                                Ticket list
                            </Button>
                        </div>
                        <div className="ticket-data-container__title">
                            <Typography variant="h4" color={'primary'}>{`Ticket №${id}  - "${name}"`}</Typography>
                        </div>
                        <div className="ticket-data-container__info">
                            <TableContainer className="ticket-table" component={Paper}>
                                <Table>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Created on:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {createdOn}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Category:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {category}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Status:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {state}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Urgency:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {urgency}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Desired Resolution Date:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {desiredResolutionDate}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Owner:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {ticketOwner}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Approver:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {approver || "Not assigned"}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Assignee:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {assignee || "Not assigned"}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Attachments:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                {attachment == '' && (
                                                    <Typography align="left" variant="subtitle1">
                                                        "Attachment is missing"
                                                    </Typography>
                                                )}
                                                {attachment !== "" && (
                                                    <Typography align="left" variant="subtitle1">
                                                        {Object.keys(attachment).map((el) => {
                                                            return (
                                                                <Button
                                                                    size={"small"}
                                                                    onClick={() => handleDownloadAttachment(attachment[el].id, attachment[el].name)}
                                                                    variant="contained"
                                                                    color="primary"
                                                                >
                                                                    {attachment[el].name}
                                                                </Button>
                                                            )
                                                        })}
                                                    </Typography>
                                                )}
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Description:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {description || "Not assigned"}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </div>
                        <div className="ticket-data-container__button-section">
                            {state !== "DONE" && (
                                <ButtonGroup variant="contained" color="primary">
                                    {Object.keys(action).map((el) => {
                                        return (
                                            <Button
                                                onClick={() => handleAction(action[el], id)}
                                                variant="contained"
                                                color="primary"
                                            >
                                                {action[el].split("_").join(" ")}
                                            </Button>
                                        )
                                    })}
                                    {state === "DRAFT" && (<Button
                                        component={Link}
                                        to={`/create-ticket/${id}`}
                                        onClick={handleEditTicket}
                                    >
                                        Edit
                                    </Button>)}
                                </ButtonGroup>
                            )}
                            {state === "DONE" && (
                                <ButtonGroup variant="contained" color="primary">
                                    {Object.keys(action).map((el) => {
                                        return (
                                            <Button
                                                component={Link}
                                                to={`/feedback/${id}`}
                                                variant="contained"
                                                color="primary"
                                            >
                                                {action[el].split("_").join(" ")}
                                            </Button>
                                        )
                                    })}
                                </ButtonGroup>
                            )}

                            {feedbackId !== null && (
                                <ButtonGroup variant="contained" color="primary">
                                    return (
                                    <Button
                                        component={Link}
                                        to={`/feedback/${id}`}
                                        variant="contained"
                                        color="primary"
                                    >
                                        View feedback
                                    </Button>
                                    )
                                    })}
                                    {state === "DRAFT" && (<Button
                                        component={Link}
                                        to={`/create-ticket/${id}`}
                                        onClick={handleEditTicket}
                                    >
                                        Edit
                                    </Button>)}
                                </ButtonGroup>
                            )}
                        </div>
                        <div className="ticket-data-container__comments-section comments-section">
                            <div className="">
                                <Tabs
                                    variant="fullWidth"
                                    onChange={this.handleTabChange}
                                    value={tabValue}
                                    indicatorColor="primary"
                                    textColor="primary"
                                >
                                    <Tab label="History" {...a11yProps(0)} />
                                    <Tab label="Comments" {...a11yProps(1)} />
                                </Tabs>
                                <TabPanel value={tabValue} index={0}>
                                    <HistoryTable history={ticketHistory}/>
                                </TabPanel>
                                <TabPanel value={tabValue} index={1}>
                                    <CommentsTable comments={ticketComments}/>
                                </TabPanel>
                            </div>
                        </div>
                        {tabValue && (
                            <div className="ticket-data-container__enter-comment-section enter-comment-section">
                                <TextField
                                    label="Enter a comment"
                                    multiline
                                    rows={4}
                                    value={commentValue}
                                    variant="filled"
                                    className="comment-text-field"
                                    onChange={this.handleEnterComment}
                                />
                                <div className="enter-comment-section__add-comment-button">
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={this.addComment}
                                    >
                                        Add Comment
                                    </Button>
                                </div>
                            </div>
                        )}
                    </div>
                </Route>
                <Route path="/create-ticket/:ticketId">
                    <TicketCreationPageWithRouter/>
                </Route>
            </Switch>
        );
    }
}

TicketInfo.propTypes = {
    match: PropTypes.object,
};

const TicketInfoWithRouter = withRouter(TicketInfo);
export default TicketInfoWithRouter;
