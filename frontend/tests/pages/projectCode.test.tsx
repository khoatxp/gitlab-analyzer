import React from 'react';

import Index from '../../pages/project/[projectId]/code';
import {render} from "@testing-library/react";

describe("Pages Project Code", () =>{

    it("Snapshot Index", () => {
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();

    })


})