import LocalizedStrings from 'react-localization';

let strings = new LocalizedStrings({

    en: {
        menu: {
            Home: 'Home',
            Products: 'Products',
            Services: 'Services',
            OnlineGoods: 'Online Goods',
            EmeraldDragon: 'Emerald Dragon',
            Features: 'Features'

        },

        table: {
            actions: 'Actions',
            view: 'View',
            edit: 'Edit',
            delete: 'Revoke',
            confirmDelete: 'Confirm revoke',
            no: 'No',
            yes: 'Yes',
            search: 'Search'
        },

        header: {
            lock: 'Lock',
            logout: 'Logout'
        },

        filter: {
            search: 'Search'
        },

        validation: {
            RequiredErrorMessage: 'required',
            MinLengthErrorMessage: 'Minimal length is ',
            MaxLengthErrorMessage: 'Maximal length is ',
            EmailErrorMessage: 'Please enter valid email',
            PasswordErrorMessage: 'Password must contain at least 6 letters, one upper case, one lower case and one number.',
            UserExistsErrorMessage: 'User with this email address already exists',
            OldPasswordDidNotMatch: 'Old password did not match',
            PasswordsNotEqual: 'Passwords do not match',
            notNumber: 'Not number'
        },

        notFound: {
            notFound: 'Not found!',
            dashboard: 'Dashboard'
        },

        forbidden: {
            forbidden: 'Forbidden!',
            dashboard: 'Dashboard'
        },

        error: {
            error: 'Error!',
            dashboard: 'Dashboard'
        },

        login: {
            email: 'Email',
            password: 'Password',
            login: 'Login',
            wrongCredentials: 'Wrong Credentials'
        },

        lock: {
            password: 'Password',
            login: 'Login',
            wrongCredentials: 'Wrong Credentials',
            unlock: 'Unlock'
        },

        userList: {
            firstName: 'First Name',
            lastName: 'Last Name',
            email: 'Email',
            role: 'Role',
            isDeleted: 'Is deleted',
            dateCreated: 'Date Created',
            pageTitle: 'Users',
            enabled: 'Enabled',
            userDelete: 'User deleted',
            userRestored: 'User restored'
        },

        userForm: {
            email: 'Email',
            firstName: 'First name',
            lastName: 'Last name',
            ok: 'Ok',
            cancel: 'Cancel',
            password: 'Password'
        },

        addUser: {
            pageTitle: 'Add user',
            errorAddClub: 'Error adding user',
            clubAdded: 'User added',
            errorAddingUser: 'Error adding user',
            userAdded: 'User added'
        },

        resetPassword: {
            email: 'Email',
            resetPassword: 'Reset password',
            password: 'Password',
            passwordRepeat: 'Password repeat'
        },
    }
});

export default strings;