import React from 'react';

import Index from '../../pages';
import {render} from "@testing-library/react";

describe("Pages Root", () =>{
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();

    beforeAll(() =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
    })

    it("Snapshot Index", () => {
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();

    })


})