import './ContentEditor.css';

import { useEffect, useCallback } from 'react';
import { EditorContent, useEditor } from '@tiptap/react';
import { StarterKit } from '@syfxlin/tiptap-starter-kit';
import toast from 'react-hot-toast';
import { savePublication, enhancePublication } from '../../services/client';
import { Markdown } from 'tiptap-markdown';
import TextAlign from '@tiptap/extension-text-align';
import { useNavigate } from 'react-router-dom';
import { useFormik } from 'formik';
import * as Yup from 'yup';

const loadDraft = () => {
    const draft = JSON.parse(localStorage.getItem('draft') || '{}');
    return {
        title: draft.title || '',
        author: draft.author || '',
        content: draft.content || 'This is a basic example of usage. Press `/` to see available commands. Click on Image to resize and align. ![](https://placehold.co/800x400/6A00F5/white)',
        ttl: draft.ttl || 1,
        ttlUnit: draft.ttlUnit || 'hours',
    };
};

const ttlToMinutes = ({ ttl, ttlUnit }) => {
    if (ttlUnit === 'minutes') return ttl;
    if (ttlUnit === 'hours') return ttl * 60;
    if (ttlUnit === 'days') return ttl * 60 * 24;
}

export default function ContentEditor() {
    const navigate = useNavigate();

    const formik = useFormik({
        initialValues: loadDraft(),

        validationSchema: Yup.object({
            title: Yup.string()
                .max(255, 'Title is too long!')
                .required('Title is required!'),
            author: Yup.string()
                .max(255, 'Author\'s name is too long!'),
            content: Yup.string()
                .min(20, 'Content is too short!')
                .max(128000, 'Content is too long!')
                .required('Content is required!'),
            ttl: Yup.number()
                .required('TTL is required!')
                .test('ttl-in-minutes', 'Expiration time is too small!', function (value) {
                    const { ttlUnit } = this.parent;
                    return ttlToMinutes({ ttl: value, ttlUnit }) >= 1;
                })
                .test('ttl-in-minutes-max', 'Expiration time is too big!', function (value) {
                    const { ttlUnit } = this.parent;
                    return ttlToMinutes({ ttl: value, ttlUnit }) <= 43830;
                }),
        }),

        onSubmit: async (values) => {

            if (!formik.isValid) {
                toast.error(Object.values(formik.errors)[0]);
                return;
            }

            try {
                const response = await toast.promise(
                    savePublication(
                        values.title,
                        values.author,
                        values.content,
                        ttlToMinutes({ ttl: values.ttl, ttlUnit: values.ttlUnit })
                    ),
                    {
                        loading: 'Publishing...',
                        success: <b>Successfully published!</b>,
                        error: <b>Something went wrong!</b>,
                    }
                );

                const hash = response.data.hash;
                localStorage.removeItem('draft');
                navigate(`/p/${hash}`);

            } catch (e) {
                console.error(e);
            }
        },
    });

    const editor = useEditor({
        extensions: [
            StarterKit.configure({ slashMenu: true, floatMenu: false }),
            Markdown,
            TextAlign.configure({
                types: ['heading', 'paragraph'],
                defaultAlignment: 'left',
            }),
        ],
        content: formik.values.content,
        autofocus: true,
        onUpdate({ editor }) {
            const markdownContent = editor.storage.markdown.getMarkdown();
            formik.setFieldValue('content', markdownContent);
        },
        onBlur() {
            formik.handleBlur({ target: { name: 'content' } });
        }
    });

    const handleEnhance = useCallback(async () => {
        if (!formik.isValid) {
            toast.error(Object.values(formik.errors)[0]);
            return;
        }

        try {
            const response = await toast.promise(
                enhancePublication(formik.values.title, formik.values.content),
                {
                    loading: 'Just a moment, please...',
                    success: <b>Enhanced!</b>,
                    error: <b>Something went wrong!</b>,
                }
            );
            const enhancedMarkdown = response.data;
            formik.setFieldValue('content', enhancedMarkdown);
            editor?.commands.setContent(enhancedMarkdown);

        } catch (e) {
            console.error(e);
        }
    }, [editor, formik]);

    useEffect(() => {
        localStorage.setItem('draft', JSON.stringify(formik.values));
    }, [formik.values]);

    return (
        <>
            <form className="editor-container" onSubmit={formik.handleSubmit}>
                <div className="form-field">
                    <input
                        type="text"
                        name="title"
                        className="input-field"
                        placeholder="Title"
                        value={formik.values.title}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                </div>

                <div className="form-field">
                    <input
                        type="text"
                        name="author"
                        className="input-field author"
                        placeholder="Author"
                        value={formik.values.author}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                    />
                </div>

                <EditorContent editor={editor} />
            </form>

            <div className="controls">
                <div className="container">
                    <label htmlFor="ttl-input">Publication Expiration</label>
                    <input
                        id="ttl-input"
                        name="ttl"
                        className="ttl-input"
                        type="number"
                        min="1"
                        value={formik.values.ttl}
                        onChange={formik.handleChange}
                    />
                    <select
                        name="ttlUnit"
                        className='ttl-select'
                        value={formik.values.ttlUnit}
                        onChange={formik.handleChange}
                    >
                        <option value="minutes">Minutes</option>
                        <option value="hours">Hours</option>
                        <option value="days">Days</option>
                    </select>
                </div>

                <div className="container">
                    <button
                        type="button"
                        className='btn btn-secondary'
                        onClick={handleEnhance}
                    >
                        Enhance
                    </button>
                    <button
                        type="button"
                        className='btn btn-primary'
                        onClick={async () => {
                            if (!formik.isValid) {
                                toast.error(Object.values(formik.errors)[0]);
                                return;
                            }
                            formik.handleSubmit();
                        }}
                    >
                        Publish
                    </button>
                </div>
            </div>
        </>
    );
}