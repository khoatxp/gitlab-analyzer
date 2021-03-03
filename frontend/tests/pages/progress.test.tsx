import React from 'react';

import Index from '../../pages/progress';
import {render} from "@testing-library/react";

describe("Pages Progress", () =>{
    const mockUseEffect = jest.spyOn(React, 'useEffect')

    it("Snapshot Index", () => {
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();
    })
    it("Test useEffect", ()=>{
        render(<Index />);
        expect(mockUseEffect).toBeCalled();
    })


})