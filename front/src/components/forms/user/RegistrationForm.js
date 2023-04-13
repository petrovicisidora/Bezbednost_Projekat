import React from 'react';
import strings from "../../../localization";
import { getError, hasError } from "../../../functions/Validation";
import { Button, TextField } from "@material-ui/core";
import Select from 'react-select'





const RegistrationForm = ({
    onSubmit,
    onCancel,
    onChange,
    errors,
    data
}) => {
      

    return(
        <form id='user-form'>
            <TextField
                label={strings.userForm.email}
                error={hasError(errors, 'email')}
                helperText={getError(errors, 'email')}
                fullWidth
                autoFocus
                name='email'
                onChange={onChange}
                margin="normal"
                value={data.email}
            />
            <TextField
                label={strings.userForm.firstName}
                error={hasError(errors, 'firstName')}
                helperText={getError(errors, 'firstName')}
                fullWidth
                autoFocus
                name='firstName'
                onChange={onChange}
                margin="normal"
                value={data.firstName}
            />
            <TextField
                label={strings.userForm.lastName}
                error={hasError(errors, 'lastName')}
                helperText={getError(errors, 'lastName')}
                fullWidth
                autoFocus
                name='lastName'
                onChange={onChange}
                margin="normal"
                value={data.lastName}
            />

           
            <TextField
                label={strings.userForm.password}
                error={hasError(errors, 'password')}
                helperText={getError(errors, 'password')}
                fullWidth
                autoFocus
                name='password'
                onChange={onChange}
                margin="normal"
                value={data.password}
            />
            <div className='submit-container'>
                <Button variant="contained" color="primary" onClick={onSubmit}>
                    {strings.userForm.ok}
                </Button>
                <Button variant="contained" color="secondary" onClick={onCancel}>
                    {strings.userForm.cancel}
                </Button>
            </div>
        </form>
);}

export default RegistrationForm;