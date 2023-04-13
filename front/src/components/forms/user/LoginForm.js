import React from 'react';
import strings from '../../../localization';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { getError, hasError } from "../../../functions/Validation";

const LoginForm = ({
    onSubmit,
    onChange,
    errors,
    data,
    keyPress,
    onRegistration
}) => (
        <form id="login-form" onSubmit={onSubmit} action="#">
            <TextField
                label={strings.login.email}
                error={hasError(errors, 'email')}
                helperText={getError(errors, 'email')}
                fullWidth
                autoFocus
                name='email'
                onChange={onChange}
                onKeyPress={keyPress}
                margin="normal"
                value={data.email}
            />

            <TextField
                label={strings.login.password}
                error={hasError(errors, 'password')}
                helperText={getError(errors, 'password')}
                fullWidth
                name='password'
                type='password'
                onChange={onChange}
                onKeyPress={keyPress}
                margin="normal"
                value={data.password}
            />

            <div className='submit-container'>
                <Button variant="contained" color="primary" onClick={onSubmit}>
                    {strings.login.login}
                </Button>
                <Button variant="contained" color="primary" onClick={onRegistration} style={{ marginLeft: '15px' }}>
                    Registration
            </Button>
            </div>
        </form>
    );

export default LoginForm;