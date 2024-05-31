import React from "react";
import {
    Button, ButtonGroup,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Typography,
} from "@material-ui/core";
import {Link, withRouter} from "react-router-dom";
import {URGENCY_OPTIONS} from "../constants/inputsValues";
import {Redirect} from "react-router";


class TicketCreationPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            id: null,
            nameValue: "",
            descriptionValue: "",
            urgencyValue: "LOW",
            desiredResolutionDateValue: "",
            categoryValue: "Application & Services",
            commentValue: null,
            redirect: false,
            categories: [],
            attachmentValue: [],
            selectedAttachment: "",
            ticketId: "",
            error: []
        };

    }

    componentDidMount() {
        this.performEditTicket()
    }

    performEditTicket() {
        if (localStorage.getItem('authorization') !== null) {
            const url = this.props.location.pathname;
            const id = url.split('/').pop();

            if (id !== "create-ticket") {

                this.setState({
                        id: id,
                        attachmentValue: []
                    }
                );

                fetch('http://localhost:8080/ticket/' + id, {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                        'Content-Type': 'application/json;charset=utf-8'
                    }
                }).then(response => response.json())
                    .then(json => {
                        this.setState({
                            ticketId: json.id,
                            nameValue: json.name,
                            descriptionValue: json.description,
                            urgencyValue: json.urgency,
                            categoryValue: json.category,
                            desiredResolutionDateValue: json.desiredResolutionDate,
                            selectedAttachment: json.attachment
                        });
                    });
            }


            fetch('http://localhost:8080/category', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({
                        categories: json
                    });
                });
        }
    }


    createOrUpdateTicket = async (status, method, url) => {

        const response = await fetch(url.toString(), {
            method: method,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                name: this.state.nameValue,
                description: this.state.descriptionValue,
                urgency: this.state.urgencyValue,
                state: status,
                desiredResolutionDate: this.state.desiredResolutionDateValue,
                category: this.state.categoryValue
            })
        }).catch(() => {
            console.log("Error ticket creation")
        })

        const ticketResult = await response.json();
        if (response.ok) {
            this.setState({
                id: ticketResult,
                ticketId: ticketResult
            })
            await this.addAttachment(response, ticketResult);
            await this.addComment(response, ticketResult);
        } else {
            this.state.error.push(ticketResult.message)
            this.performEditTicket()
        }

        if (response.ok && this.state.error.length === 0) {
            this.setState({
                redirect: true
            });
        }

    }

    addAttachment = async (response, id) => {
        if (response.ok) {
            if (this.state.attachmentValue.length > 0) {
                const attachmentFiles = this.state.attachmentValue;
                let formData = new FormData()
                for (let i = 0; i < attachmentFiles.length; i++) {
                    formData.append(
                        'file', attachmentFiles[i]
                    );
                }
                const attachmentUrl = new URL('http://localhost:8080/attachment/' + id);

                let attachmentResponse = await fetch(attachmentUrl.toString(), {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    },
                    body: formData
                }).catch(() => {
                    console.log("Error adding attachment")
                })

                if (!attachmentResponse.ok) {
                    let attachmentResult = await attachmentResponse.json();
                    this.state.error.push(attachmentResult.message);
                    this.performEditTicket();
                }
            }
        }
    }

    addComment = async (response, id) => {
        if (response.ok) {
            if (this.state.commentValue !== null) {
                const commentUrl = new URL('http://localhost:8080/comment');
                commentUrl.searchParams.append("ticketId", id);
                let commentResponse = await fetch(commentUrl.toString(), {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                        'Content-Type': 'application/json;charset=utf-8'
                    },
                    body: JSON.stringify({
                        text: this.state.commentValue
                    })
                }).catch(() => {
                    console.log("Error adding comment")
                })

                if (!commentResponse.ok) {
                    let commentResult = await commentResponse.json();
                    this.state.error.push(commentResult.message)
                    this.performEditTicket()
                }
            }
        }
    }

    handleSaveDraft = async () => {
        this.setState({
            error: []
        })
        let url = new URL('http://localhost:8080/ticket');
        let method = 'POST';
        if (this.state.id !== null) {
            method = 'PUT';
            url.searchParams.append('ticketId', this.state.id)
        }
        await this.createOrUpdateTicket("DRAFT", method, url);
    };

    handleSubmitTicket = async () => {
        this.setState({
            error: []
        })
        let url = new URL('http://localhost:8080/ticket');
        let method = 'POST';
        if (this.state.id !== null) {
            method = 'PUT';
            url.searchParams.append('ticketId', this.state.id)
        }
        await this.createOrUpdateTicket("NEW", method, url);
    };

    handleCategoryChange = (event) => {
        this.setState({
            categoryValue: event.target.value,
        });

    };

    handleNameChange = (event) => {
        this.setState({
            nameValue: event.target.value,
        });
    };

    handleDescriptionChange = (event) => {
        this.setState({
            descriptionValue: event.target.value,
        });
    };

    handleUrgencyChange = (event) => {
        this.setState({
            urgencyValue: event.target.value,
        });
    };

    handleResolutionDate = (event) => {
        this.setState({
            desiredResolutionDateValue: event.target.value,
        });
    };

    handleAttachmentChange = (event) => {
        for (let i = 0; i < event.target.files.length; i++) {
            this.state.attachmentValue.push(event.target.files[i])
        }
        this.setState({})
    };

    handleRemoveAttachment = (event) => {
        let filteredArray = this.state.attachmentValue.filter(item => item !== event)
        this.setState({
            attachmentValue: filteredArray
        });
    };

    handleDeleteAttachment = async (event) => {
        let url = new URL('http://localhost:8080/attachment/' + this.state.id + "/" + event);
        let res = await fetch(url.toString(), {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            }
        })
        if (res.ok) {
            fetch('http://localhost:8080/ticket/' + this.state.id, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({
                        selectedAttachment: json.attachment
                    });
                });
        }
        this.setState({});
    }

    handleCommentChange = (event) => {
        this.setState({
            commentValue: event.target.value,
        });
    };


    render() {
        const {
            id,
            nameValue,
            categoryValue,
            commentValue,
            descriptionValue,
            desiredResolutionDateValue,
            urgencyValue,
            redirect,
            categories,
            attachmentValue,
            ticketId,
            selectedAttachment,
            error
        } = this.state;


        const {handleAttachmentChange, handleRemoveAttachment, handleDeleteAttachment} = this

        if (redirect) {
            return <Redirect to={`/ticket-info/${ticketId}`}/>;
        }

        if (localStorage.getItem('authorization') === null) {
            return <Redirect to={`/`}/>;
        }

        return (
            <div className="ticket-creation-form-container">
                <header className="buttons-container">
                    <Button color={"primary"} component={Link} to="/main-page" variant="contained">
                        Ticket List
                    </Button>
                    {id !== null && (
                        <Button color={"primary"} component={Link} to={`/ticket-info/${id}`} variant="contained">
                            Overview
                        </Button>
                    )}
                </header>
                {id !== null && (
                    <div className="ticket-creation-form-container__title">
                        <Typography display="block" variant="h3" color={"primary"}>
                            EDIT TICKET #{id}
                        </Typography>
                    </div>
                )}
                {id === null && (
                    <div className="ticket-creation-form-container__title">
                        <Typography display="block" variant="h3" color={"primary"}>
                            CREATE NEW TICKET
                        </Typography>
                    </div>
                )}
                <div className="ticket-creation-form-container__form">
                    <div className="inputs-section">
                        <div
                            className="ticket-creation-form-container__inputs-section inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
                            <FormControl>
                                <TextField
                                    required
                                    label="Name"
                                    variant="outlined"
                                    onChange={this.handleNameChange}
                                    id="name-label"
                                    value={nameValue}
                                />
                            </FormControl>
                        </div>
                        <div
                            className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
                            <FormControl variant="outlined" required>
                                <InputLabel shrink htmlFor="category-label">
                                    Category
                                </InputLabel>
                                <Select
                                    value={categoryValue}
                                    onChange={this.handleCategoryChange}
                                >
                                    {categories.map((item, id) => {
                                        return (
                                            <MenuItem value={item.name} key={id}>
                                                {item.name}
                                            </MenuItem>
                                        );
                                    })}
                                </Select>
                            </FormControl>
                        </div>
                        <div className="inputs-section__ticket-creation-input ticket-creation-input">
                            <FormControl variant="outlined" required>
                                <InputLabel shrink htmlFor="urgency-label">
                                    Urgency
                                </InputLabel>
                                <Select
                                    value={urgencyValue}
                                    label="Urgency"
                                    onChange={this.handleUrgencyChange}
                                    className={"ticket-creation-input_width200"}
                                    inputProps={{
                                        name: "urgency",
                                        id: "urgency-label",
                                    }}
                                >
                                    {URGENCY_OPTIONS.map((item, index) => {
                                        return (
                                            <MenuItem value={item.value} key={index}>
                                                {item.label}
                                            </MenuItem>
                                        );
                                    })}
                                </Select>
                            </FormControl>
                        </div>
                    </div>
                    <div className="inputs-section-attachment">
                        <div
                            className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
                            <FormControl>
                                <InputLabel shrink htmlFor="urgency-label">
                                    Desired resolution date
                                </InputLabel>
                                <TextField
                                    onChange={this.handleResolutionDate}
                                    label="Desired resolution date"
                                    type="date"
                                    id="resolution-date"
                                    value={desiredResolutionDateValue}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                />
                            </FormControl>
                        </div>
                        <div className="ticket-creation-input">
                            <FormControl>
                                <Typography variant="caption">Add attachment</Typography>
                                <TextField
                                    type="file"
                                    multiple
                                    variant="outlined"
                                    onChange={handleAttachmentChange}
                                />
                            </FormControl>
                        </div>
                    </div>
                    {attachmentValue.length !== 0 && (
                        <div align={"center"}>
                            <h6 align={"center"}>Attached files:</h6>
                            <ButtonGroup variant="contained" color="primary">
                                {attachmentValue.map((item) => {
                                    return (
                                        <Button
                                            onClick={() => handleRemoveAttachment(item)}
                                            variant="contained"
                                            color="secondary"
                                            size={"small"}
                                        >
                                            {item.name}
                                        </Button>
                                    )
                                })}
                            </ButtonGroup>
                        </div>
                    )}
                    {selectedAttachment.length !== 0 && (
                        <div align={"center"}>
                            <h6 align={"center"}>Uploaded files:</h6>
                            <ButtonGroup variant="contained" color="primary">
                                {selectedAttachment.map((item) => {
                                    return (
                                        <Button
                                            onClick={() => handleDeleteAttachment(item.id)}
                                            variant="contained"
                                            color="secondary"
                                            size={"small"}
                                        >
                                            {item.name}
                                        </Button>
                                    )
                                })}
                            </ButtonGroup>
                        </div>
                    )}
                    <div className="inputs-section">
                        <FormControl>
                            <TextField
                                label="Description"
                                multiline
                                rows={3}
                                variant="outlined"
                                value={descriptionValue}
                                className="creation-text-field creation-text-field_width680"
                                onChange={this.handleDescriptionChange}
                            />
                        </FormControl>
                    </div>
                    <div className="inputs-section">
                        <FormControl>
                            <TextField
                                label="Comment"
                                multiline
                                rows={3}
                                variant="outlined"
                                value={commentValue}
                                className="creation-text-field creation-text-field_width680"
                                onChange={this.handleCommentChange}
                            />
                        </FormControl>
                    </div>
                    <section className="submit-button-section">
                        <Button variant="contained"
                                onClick={this.handleSaveDraft}>
                            Save as Draft
                        </Button>
                        <Button
                            variant="contained"
                            onClick={this.handleSubmitTicket}
                            color="primary"
                        >
                            Submit
                        </Button>
                    </section>
                    {error.length !== 0 && (
                        <div align={"center"}>
                            <br/>
                            <Typography display="block" variant="h3" color={"secondary"}> ERROR</Typography>
                            {error.map((el) => {
                                    return (
                                        <Typography color={"secondary"}>
                                            {el}
                                        </Typography>
                                    )
                                }
                            )}

                        </div>
                    )}
                </div>
            </div>
        );
    }
}

const
    TicketCreationPageWithRouter = withRouter(TicketCreationPage);
export default TicketCreationPageWithRouter;